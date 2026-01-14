package com.swdev.springbootproject.service;

import com.swdev.springbootproject.model.tmdb.TmdbGenre;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import com.swdev.springbootproject.model.tmdb.TmdbResults;
import com.swdev.springbootproject.model.tmdb.TmdbTv;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class TMDBService {
  public static final String API_URL = "https://api.themoviedb.org/3/";

  public static final String ENDPOINT_DISCOVER_MOVIE = "/discover/movie";
  public static final String ENDPOINT_MOVIE = "/movie/{id}";
  public static final String ENDPOINT_SEARCH_MOVIE = "/search/movie";
  public static final String ENDPOINT_SEARCH_TV = "/search/tv";

  public static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342/";
  public static final String BACKDROP_BASE_URL = "https://image.tmdb.org/t/p/w1280/";

  private final RestClient restClient = RestClient.builder().baseUrl(API_URL).build();

  private final LocaleService localeService;

  @Value("${tmdb.api.key:keynotfound}")
  private String apiKey;

  public List<TmdbMovie> getPopularMovies(int page) {
    return Objects.requireNonNull(
            this.restClient
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path(ENDPOINT_DISCOVER_MOVIE)
                            .queryParam("include_adult", false)
                            .queryParam("include_video", false)
                            .queryParam("page", page)
                            .queryParam("sort_by", "popularity.desc")
                            .queryParam("certification_country", "US")
                            .queryParam("certification.lte", "PG-13")
                            .queryParam("language", localeService.getCurrentLanguage())
                            .queryParam("api_key", apiKey)
                            .build())
                .retrieve()
                .body(new ParameterizedTypeReference<@NonNull TmdbResults<TmdbMovie>>() {}))
        .getResults();
  }

  public List<TmdbMovie> getMoviesByMood(String mood, int page) {
    Map<String, List<TmdbGenre>> moodToGenre =
        new HashMap<>(
            Map.ofEntries(
                Map.entry("adventurous", List.of(TmdbGenre.ACTION, TmdbGenre.FANTASY)),
                Map.entry("happy", List.of(TmdbGenre.COMEDY, TmdbGenre.FAMILY)),
                Map.entry("brave", List.of(TmdbGenre.HORROR)),
                Map.entry("nostalgic", List.of(TmdbGenre.HISTORY)),
                Map.entry("romantic", List.of(TmdbGenre.COMEDY, TmdbGenre.ROMANCE)),
                Map.entry("sad", List.of(TmdbGenre.DRAMA)),
                Map.entry("contemplative", List.of(TmdbGenre.SCIFI, TmdbGenre.ACTION)),
                Map.entry("curious", List.of(TmdbGenre.DOCUMENTARY)),
                Map.entry("edgy", List.of(TmdbGenre.CRIME)),
                Map.entry("silly", List.of(TmdbGenre.ANIMATION, TmdbGenre.COMEDY)),
                Map.entry("cozy", List.of(TmdbGenre.FAMILY, TmdbGenre.ANIMATION)),
                Map.entry("detective-like", List.of(TmdbGenre.MYSTERY)),
                Map.entry("melodic", List.of(TmdbGenre.MUSIC))));

    List<TmdbGenre> genres = moodToGenre.getOrDefault(mood, List.of(TmdbGenre.ADVENTURE));

    String genreParam =
        genres.stream()
            .map(TmdbGenre::getGenreId)
            .map(String::valueOf)
            .collect(Collectors.joining(","));

    final var discoverResults =
        this.restClient
            .get()
            .uri(
                uriBuilder ->
                    uriBuilder
                        .path(ENDPOINT_DISCOVER_MOVIE)
                        .queryParam("include_adult", false)
                        .queryParam("include_video", false)
                        .queryParam("page", page)
                        .queryParam("sort_by", "popularity.desc")
                        .queryParam("certification_country", "US")
                        .queryParam("certification.gte", "G")
                        .queryParam("certification.lte", "PG-13")
                        .queryParam("with_genres", genreParam)
                        .queryParam("language", localeService.getCurrentLanguage())
                        .queryParam("api_key", apiKey)
                        .build())
            .retrieve()
            .body(new ParameterizedTypeReference<@NonNull TmdbResults<TmdbMovie>>() {});

    if (discoverResults == null) {
      return List.of();
    }
    return discoverResults.getResults();
  }

  public TmdbMovie getMovieDetails(Long movieId) {
    return restClient
        .get()
        .uri(
            uriBuilder ->
                uriBuilder
                    .path(ENDPOINT_MOVIE)
                    .queryParam("api_key", apiKey)
                    .queryParam("language", localeService.getCurrentLanguage())
                    .build(movieId))
        .retrieve()
        .body(TmdbMovie.class);
  }

  public List<TmdbMovie> searchMovies(String query) {
    return Objects.requireNonNull(
            restClient
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path(ENDPOINT_SEARCH_MOVIE)
                            .queryParam("query", query)
                            .queryParam("language", localeService.getCurrentLanguage())
                            .queryParam("api_key", apiKey)
                            .build())
                .retrieve()
                .body(new ParameterizedTypeReference<@NonNull TmdbResults<TmdbMovie>>() {}))
        .getResults();
  }

  public List<TmdbTv> searchTv(String query) {
    return Objects.requireNonNull(
            restClient
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path(ENDPOINT_SEARCH_TV)
                            .queryParam("query", query)
                            .queryParam("language", localeService.getCurrentLanguage())
                            .queryParam("api_key", apiKey)
                            .build())
                .retrieve()
                .body(new ParameterizedTypeReference<@NonNull TmdbResults<TmdbTv>>() {}))
        .getResults();
  }

  public List<TmdbMovie> getUpcomingMovies(int page) {
    return Objects.requireNonNull(
            this.restClient
                .get()
                .uri(
                    uriBuilder ->
                        uriBuilder
                            .path(ENDPOINT_DISCOVER_MOVIE)
                            .queryParam("include_adult", false)
                            .queryParam("include_video", false)
                            .queryParam("page", page)
                            .queryParam("sort_by", "release_date.desc")
                            .queryParam("certification_country", "US")
                            .queryParam("certification.lte", "PG-13")
                            .queryParam("language", localeService.getCurrentLanguage())
                            .queryParam("api_key", apiKey)
                            .build())
                .retrieve()
                .body(new ParameterizedTypeReference<@NonNull TmdbResults<TmdbMovie>>() {}))
        .getResults();
  }
}
