package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.PostToPostDtoConverter;
import com.swdev.springbootproject.component.QueryParamToBookmarkStatusConverter;
import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.MovieBookmark;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.MovieBookmarkRepository;
import com.swdev.springbootproject.repository.MovieRepository;
import com.swdev.springbootproject.repository.PostRepository;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
  private final PostRepository postRepository;
  private final PostToPostDtoConverter postToPostDto;

  @GetMapping("/{id}")
  public String movie(@PathVariable Long id, Model model) {
    final var movie = tmdbService.getMovieDetails(id);
    final var posts =
        postRepository
            .findAllByMovies_Id(movie.getId(), PageRequest.of(0, 10, Sort.by("id").descending()))
            .stream()
            .map(postToPostDto::convert)
            .toList();

    model.addAttribute("posts", posts);

    model.addAttribute("movieDetails", movie);
    model.addAttribute("poster", TMDBService.POSTER_BASE_URL + movie.getPosterPath());
    model.addAttribute("backdrop", TMDBService.BACKDROP_BASE_URL + movie.getBackdropPath());
    model.addAttribute("id", id);
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

    final var movie = movieRepository.save(CbMovie.builder().id(id).build());

    movieBookmarkRepository.save(
        new MovieBookmark(
            currentUser, movie, queryParamToBookmarkStatusConverter.convert(category)));

    return "redirect:/app/movie/" + id;
  }

  @DeleteMapping("/{id}/unbookmark")
  public String unbookmark(@PathVariable Long id, Authentication authentication) {
    var currentUser = (CbUser) authentication.getPrincipal();

    if (currentUser == null) {
      throw new IllegalArgumentException("User is not logged in");
    }

    movieBookmarkRepository
        .findByUserAndMovie(currentUser, CbMovie.builder().id(id).build())
        .ifPresent(movieBookmarkRepository::delete);

    return "redirect:/app/movie/" + id;
  }

  @GetMapping("/bookmarkDropdown")
  public String bookmarkDropdown(
      @RequestParam Long id, Model model, Authentication authentication) {
    final var bookmark =
        movieBookmarkRepository.findByUserAndMovie(
            (CbUser) authentication.getPrincipal(), CbMovie.builder().id(id).build());

    model.addAttribute("id", id);
    model.addAttribute("activeTab", bookmark.map(MovieBookmark::getStatus).orElse(null));

    return "movie_details :: bookmarkDropdown";
  }
}
