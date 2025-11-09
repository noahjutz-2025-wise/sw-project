package com.swdev.springbootproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;



@Service
public class TMDBService {
    private final RestClient restClient;

    @Value("${tmdb.api.key:keynotfound}")
    private String apiKey;

    public TMDBService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("https://api.themoviedb.org/3").build();
    }

    public String getPopularMovies() {
        return this.restClient.get().uri("/movie/popular?api_key=" + apiKey).retrieve().body(String.class);
    }
}
