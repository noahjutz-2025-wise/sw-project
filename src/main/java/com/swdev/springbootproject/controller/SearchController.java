package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/search")
class SearchController {
  private final TMDBService tmdbService;

  @GetMapping("")
  public String search() {
    return "search";
  }

  @GetMapping("results")
  public String searchResults(Model model) {
    final var movies = tmdbService.getPopularMovies(1).subList(0, new Random().nextInt(10));
    model.addAttribute("movies", movies);
    return "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
  }
}
