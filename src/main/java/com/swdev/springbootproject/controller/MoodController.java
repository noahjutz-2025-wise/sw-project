package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.service.TMDBService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoodController {
    private final TMDBService tmdbService;

    public MoodController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/api/mood")
    public List<Movie> getMood(@RequestParam(required = false) String mood) {
        return tmdbService.getMoviesByMood(mood);
    }
}
