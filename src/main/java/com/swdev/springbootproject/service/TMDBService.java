package com.swdev.springbootproject.service;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.model.TMDBApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;


@Service
public class TMDBService {
    private final RestClient restClient;

    @Value("${tmdb.api.key:keynotfound}")
    private String apiKey;

    public TMDBService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public List<Movie> getPopularMovies() {
        return Objects.requireNonNull(this.restClient
                .get()
                .uri("/movie/popular?api_key=" + apiKey)
                .retrieve()
                .body(TMDBApiResponse.class)).getResults();
    }
}
