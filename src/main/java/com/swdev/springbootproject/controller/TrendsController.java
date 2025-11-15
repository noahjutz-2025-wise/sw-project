package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrendsController {

  private final TMDBService tmdbService;

  public TrendsController(TMDBService tmdbService) {
    this.tmdbService = tmdbService;
  }

  @GetMapping("/api/trends")
  @ResponseBody
  public List<Movie> getTrends() {
    return tmdbService.getPopularMovies();
  }

  @GetMapping("/trends")
  public String showTrendingMovies(Model model) {
    model.addAttribute("movies", tmdbService.getPopularMovies());
    return "trends";
  }
}
