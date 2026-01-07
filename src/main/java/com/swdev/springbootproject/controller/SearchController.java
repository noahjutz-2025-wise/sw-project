package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.model.tmdb.Movie;
import com.swdev.springbootproject.service.TMDBService;

import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
  public String searchResults(
          @RequestParam(defaultValue = "") String search,
          @RequestParam(defaultValue = "movies") String searchType,
          @RequestParam(defaultValue = "false") boolean isIncludeSeen,
          @RequestParam(required = false) String genre,
          @RequestParam(required = false) Integer durationMin,
          @RequestParam(required = false) Integer durationMax,
          @RequestParam(required = false) Integer fsk,
          @RequestParam(required = false) Integer ratingMin,
          @RequestParam(required = false) Integer ratingMax,
          @RequestParam(required = false) Integer yearMin,
          @RequestParam(required = false) Integer yearMax,
          Model model
  ) {
    final var movies = tmdbService.getPopularMovies(1).subList(0, new Random().nextInt(10));
    model.addAttribute("movies", movies);
    return "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
  }
}
