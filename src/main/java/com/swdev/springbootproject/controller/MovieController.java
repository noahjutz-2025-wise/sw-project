package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.QueryParamToBookmarkStatusConverter;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Movie;
import com.swdev.springbootproject.entity.MovieBookmark;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.MovieBookmarkRepository;
import com.swdev.springbootproject.repository.MovieRepository;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/movie")
public class MovieController {
  private final CbUserRepository cbUserRepository;
  private final MovieBookmarkRepository movieBookmarkRepository;
  private final MovieRepository movieRepository;
  private final TMDBService tmdbService;
  private final QueryParamToBookmarkStatusConverter queryParamToBookmarkStatusConverter;

  @GetMapping("/{id}")
  public String movie(@PathVariable Long id, Model model, Authentication authentication) {
    final var movie = tmdbService.getMovieDetails(id);
    final var bookmark =
        movieBookmarkRepository.findByUserAndMovie(
            (CbUser) authentication.getPrincipal(), new Movie(id));
    model.addAttribute(movie);
    model.addAttribute("poster", TMDBService.POSTER_BASE_URL + movie.getPosterPath());
    model.addAttribute("backdrop", TMDBService.BACKDROP_BASE_URL + movie.getBackdropPath());
    model.addAttribute("id", id);
    model.addAttribute("bookmarkStatus", bookmark.map(MovieBookmark::getStatus).orElse(null));
    return "movie_details";
  }

  @PostMapping("/{id}/bookmark")
  public String bookmark(
      @PathVariable Long id, @RequestParam String category, Authentication authentication) {
    var currentUser = (CbUser) authentication.getPrincipal();

    if (currentUser == null) {
      throw new IllegalArgumentException("User is not logged in");
    }

    currentUser = cbUserRepository.findById(currentUser.getId()).orElseThrow();

    final var movie = movieRepository.save(new Movie(id));

    movieBookmarkRepository.save(
        new MovieBookmark(
            currentUser, movie, queryParamToBookmarkStatusConverter.convert(category)));

    return "redirect:/app/movie/" + id;
  }
}
