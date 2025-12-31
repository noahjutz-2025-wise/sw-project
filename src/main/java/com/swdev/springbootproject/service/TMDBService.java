package com.swdev.springbootproject.service;

import com.swdev.springbootproject.model.Genre;
import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.model.DiscoverResults;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class TMDBService {
  public static String TMDB_API_URL = "https://api.themoviedb.org/3/";

  private final RestClient restClient = RestClient.builder().baseUrl(TMDB_API_URL).build();

  @Value("${tmdb.api.key:keynotfound}")
  private String apiKey;

  public List<Movie> getPopularMovies(int page) {
    return Objects.requireNonNull(
            this.restClient
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path("/discover/movie")
                            .queryParam("include_adult", false)
                            .queryParam("include_video", false)
                            .queryParam("page", page)
                            .queryParam("sort_by", "popularity.desc")
                            .queryParam("certification_country", "US")
                            .queryParam("certification.lte", "PG-13")
                            .queryParam("api_key", apiKey)
                            .build())
                .retrieve()
                .body(DiscoverResults.class))
        .getResults();
  }

  public List<Movie> getMoviesByMood(String mood, int page) {
    Map<String, List<Genre>> moodToGenre =
        new HashMap<>(
            Map.ofEntries(
                Map.entry("adventurous", List.of(Genre.ACTION, Genre.FANTASY)),
                Map.entry("happy", List.of(Genre.COMEDY, Genre.FAMILY)),
                Map.entry("brave", List.of(Genre.HORROR)),
                Map.entry("nostalgic", List.of(Genre.HISTORY)),
                Map.entry("romantic", List.of(Genre.COMEDY, Genre.ROMANCE)),
                Map.entry("sad", List.of(Genre.DRAMA)),
                Map.entry("contemplative", List.of(Genre.SCIFI, Genre.ACTION)),
                Map.entry("curious", List.of(Genre.DOCUMENTARY)),
                Map.entry("edgy", List.of(Genre.CRIME)),
                Map.entry("silly", List.of(Genre.ANIMATION, Genre.COMEDY)),
                Map.entry("cozy", List.of(Genre.FAMILY, Genre.ANIMATION)),
                Map.entry("detective-like", List.of(Genre.MYSTERY)),
                Map.entry("melodic", List.of(Genre.MUSIC))));

    List<Genre> genres = moodToGenre.getOrDefault(mood, List.of(Genre.ADVENTURE));

    String genreParam =
        genres.stream()
            .map(Genre::getGenreId)
            .map(String::valueOf)
            .collect(Collectors.joining(","));

    DiscoverResults discoverResults =
        this.restClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path("/discover/movie")
                        .queryParam("include_adult", false)
                        .queryParam("include_video", false)
                        .queryParam("page", page)
                        .queryParam("sort_by", "popularity.desc")
                        .queryParam("certification_country", "US")
                        .queryParam("certification.gte", "G")
                        .queryParam("certification.lte", "PG-13")
                        .queryParam("with_genres", genreParam)
                        .queryParam("api_key", apiKey)
                        .build())
            .retrieve()
            .body(DiscoverResults.class);

    assert discoverResults != null;
    return discoverResults.getResults();
  }
}
