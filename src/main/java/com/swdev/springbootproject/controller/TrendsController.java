package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.service.TMDBService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TrendsController {

    private final TMDBService tmdbService;

    public TrendsController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/api/trends")
    public String getTrends() {
        return tmdbService.getPopularMovies();
    }
}
