package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToMovieDtoConverter;
import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.model.dto.MovieDto;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import com.swdev.springbootproject.service.CertifiedBangerService;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class TrendsController {

  private final TMDBService tmdbService;
  private final TmdbMovieToMovieDtoConverter tmdbMovieToMovieDto;
  private final CertifiedBangerService certifiedBangerService;

  @GetMapping("/api/trends")
  @ResponseBody
  public List<TmdbMovie> getTrends(@RequestParam(defaultValue = "1") int page) {
    return tmdbService.getPopularMovies(page);
  }

  @GetMapping("/trends")
  public String showTrendingMovies(@RequestParam(defaultValue = "1") int page, Model model, Authentication authentication) {
    List<MovieDto> movies = tmdbService.getPopularMovies(page).stream().map(tmdbMovieToMovieDto::convert).toList();

    certifiedBangerService.applyCertifiedBangerFlag(movies);

    model.addAttribute("pageTitle", "Trending Movies");
    model.addAttribute("movies", movies);
    model.addAttribute("currentPage", page);
    return "trends";
  }
}
