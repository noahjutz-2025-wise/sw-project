package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.UserMovieRating;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.MovieRepository;
import com.swdev.springbootproject.repository.UserMovieRatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    final var movie = movieRepository.save(CbMovie.builder().id(id).build());
    final var currentMovie = movieRepository.save(new CbMovie(id));

    final var userMovieRating =
              userMovieRatingRepository.findByUserAndMovie(currentUser, currentMovie);

    if (userMovieRating.isEmpty()) {
        userMovieRatingRepository.save(new UserMovieRating(currentUser, currentMovie, rating));
    }

    return "redirect:/app/rated_movies";
  }
  /*
  @DeleteMapping("/app/movie/{id}/unvote")
  public String unvoteMovie(
          @PathVariable Long id, Authentication authentication) {
      var currentUser = (CbUser) authentication.getPrincipal();

      if (currentUser == null) {
          throw new IllegalArgumentException("User is not logged in");
      }

      userMovieRatingRepository
              .findByUserAndMovie(currentUser, new CbMovie(id))
              .ifPresent(userMovieRatingRepository::delete);

      return "redirect:/app/movie/" + id;
  }
   */
}
