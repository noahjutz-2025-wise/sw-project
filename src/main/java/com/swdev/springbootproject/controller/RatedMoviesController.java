package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToMovieDtoConverter;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.UserMovieRatingRepository;
import com.swdev.springbootproject.service.TMDBService;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class RatedMoviesController {
  private final CbUserRepository cbUserRepository;
  private final TMDBService tmdbService;
  private final UserMovieRatingRepository userMovieRatingRepository;
  private final TmdbMovieToMovieDtoConverter tmdbMovieToMovieDtoConverter;

  @GetMapping("/app/rated_movies")
  public String showRatedMovies(Authentication authentication, Model model) {
    var currentUser = (CbUser) authentication.getPrincipal();

    if (currentUser == null) {
      throw new IllegalArgumentException("User is not logged in");
    }

    currentUser = cbUserRepository.findById(currentUser.getId()).orElseThrow();

    final var userMovieRating = userMovieRatingRepository.findByUser(currentUser);

    var movies =
        userMovieRatingRepository.findByUser(currentUser).stream()
            .map(votelist -> tmdbService.getMovieDetails(votelist.getMovie().getId()))
            .filter(Objects::nonNull)
            .map(tmdbMovieToMovieDtoConverter::convert)
            .collect(Collectors.toList());

    model.addAttribute("movies", movies);

    return "rated_movies";
  }
}
