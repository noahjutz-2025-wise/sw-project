package com.swdev.springbootproject.service;

import com.swdev.springbootproject.model.Genre;
import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.model.TMDBApiResponse;

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
  private final RestClient restClient =
      RestClient.builder().baseUrl("https://api.themoviedb.org/3").build();

  @Value("${tmdb.api.key:keynotfound}")
  private String apiKey;

  public List<Movie> getPopularMovies() {
    return Objects.requireNonNull(
            this.restClient
                .get()
                .uri("/movie/popular?include_adult=false&include_video=false&page=1&sort_by=popularity.desc&api_key=" + apiKey)
                .retrieve()
                .body(TMDBApiResponse.class))
        .getResults();
  }

  public List<Movie> getMoviesByMood(String mood) {
      Map<String, List<Genre>> moodToGenre = new HashMap<>(Map.ofEntries(
              Map.entry("adventurous", List.of(Genre.ADVENTURE, Genre.MYSTERY, Genre.ACTION, Genre.WESTERN)),
              Map.entry("happy", List.of(Genre.COMEDY, Genre.ANIMATION, Genre.ANIMATION, Genre.FAMILY)),
              Map.entry("brave", List.of(Genre.HORROR, Genre.THRILLER, Genre.CRIME)),
              Map.entry("nostalgic", List.of(Genre.HISTORY)),
              Map.entry("romantic", List.of(Genre.ROMANCE)),
              Map.entry("sad", List.of(Genre.DRAMA)),
              Map.entry("contemplative", List.of(Genre.SCIFI)),
              Map.entry("curious", List.of(Genre.DOCUMENTARY)),
              Map.entry("edgy", List.of(Genre.HORROR, Genre.CRIME)),
              Map.entry("silly", List.of(Genre.COMEDY)),
              Map.entry("cozy", List.of(Genre.FAMILY, Genre.ROMANCE, Genre.ANIMATION)),
              Map.entry("detective-like", List.of(Genre.MYSTERY, Genre.THRILLER))
      ));

      List<Genre> genres = moodToGenre.getOrDefault(mood, List.of(Genre.ADVENTURE));

      String genreParam = genres.stream()
              .map(Genre::getGenreId)
              .map(String::valueOf)
              .collect(Collectors.joining(","));


      TMDBApiResponse tmdbApiResponse = this.restClient
              .get()
              .uri("/discover/movie?include_adult=false&include_video=false&page=1&sort_by=popularity.desc&with_genres=" + genreParam + "&api_key=" + apiKey)
              .retrieve()
              .body(TMDBApiResponse.class);

      assert tmdbApiResponse != null;
      return tmdbApiResponse.getResults();
  }
}
