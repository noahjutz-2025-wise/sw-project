package com.swdev.springbootproject.service;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.model.TMDBApiResponse;
import java.util.List;
import java.util.Objects;
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
                .uri("/movie/popular?api_key=" + apiKey)
                .retrieve()
                .body(TMDBApiResponse.class))
        .getResults();
  }
}
