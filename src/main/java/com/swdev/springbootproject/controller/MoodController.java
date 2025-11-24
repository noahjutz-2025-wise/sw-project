package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.service.TMDBService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class MoodController {
    private final TMDBService tmdbService;

    public MoodController(TMDBService tmdbService) {
        this.tmdbService = tmdbService;
    }

    @GetMapping("/api/mood")
    public List<Movie> getMood(@RequestParam(required = false) String mood) {
        return tmdbService.getMoviesByMood(mood);
    }

    @GetMapping("/mood")
    public String showMoviesByMood(@RequestParam(name = "mood", required = false) String mood, Model model) {

        if (mood == null || mood.isBlank()) {
            mood = "happy";
        }
        model.addAttribute("mood", mood);
        model.addAttribute("movies", tmdbService.getMoviesByMood(mood));
        return "mood";
    }
}
