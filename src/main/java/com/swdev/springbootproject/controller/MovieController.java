package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/movie")
public class MovieController {
  private final CbUserRepository cbUserRepository;
  private final TMDBService tmdbService;

  @GetMapping("/{id}")
  public String movie(@PathVariable int id, Model model) {
    final var movie = tmdbService.getMovieDetails(id);
    model.addAttribute(movie);
    model.addAttribute("poster", TMDBService.POSTER_BASE_URL + movie.getPosterPath());
    model.addAttribute("backdrop", TMDBService.BACKDROP_BASE_URL + movie.getBackdropPath());
    model.addAttribute(id);
    return "movie_details";
  }

  // TODO make post mapping instead (add htmx)
  @GetMapping("/{id}/bookmark")
  public String bookmark(@PathVariable int id, @RequestParam String category) {
    // TODO insert to db
    return "movie_details";
  }
}
