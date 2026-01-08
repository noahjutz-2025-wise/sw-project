package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Movie;
import com.swdev.springbootproject.entity.UserMovieRating;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.MovieRepository;
import com.swdev.springbootproject.repository.UserMovieRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class RatingController {
  private final CbUserRepository cbUserRepository;
  private final MovieRepository movieRepository;
  private final UserMovieRatingRepository userMovieRatingRepository;

  @GetMapping("/app/movie/{id}/vote")
  public String getVoteForm(@PathVariable Long id, Model model) {
    model.addAttribute("id", id);
    return "voting";
  }

  @PostMapping("/app/movie/{id}/vote")
  public String voteMovie(
      @PathVariable Long id, @RequestParam Integer rating, Authentication authentication) {
    var currentUser = (CbUser) authentication.getPrincipal();

    if (currentUser == null) {
      throw new IllegalArgumentException("User is not logged in");
    }

    currentUser = cbUserRepository.findById(currentUser.getId()).orElseThrow();

    final var movie = movieRepository.save(new Movie(id));

    userMovieRatingRepository.save(new UserMovieRating(currentUser, movie, rating));

    return "redirect:/app/movie/" + id;
  }
}
