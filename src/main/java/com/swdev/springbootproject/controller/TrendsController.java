package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.model.Movie;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TrendsController {

  private final TMDBService tmdbService;

  public TrendsController(TMDBService tmdbService) {
    this.tmdbService = tmdbService;
  }

  @GetMapping("/api/trends")
  @ResponseBody
  public List<Movie> getTrends(@RequestParam(defaultValue = "1") int page) {
    return tmdbService.getPopularMovies(page);
  }

  @GetMapping("/trends")
  public String showTrendingMovies(@RequestParam(defaultValue = "1") int page, Model model) {
    model.addAttribute("pageTitle", "Trending Movies");
    model.addAttribute("movies", tmdbService.getPopularMovies(page));
    model.addAttribute("currentPage", page);
    return "trends";
  }
}
