package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToMovieDtoConverter;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LandingController {

  private final TMDBService tmdbService;
  private final TmdbMovieToMovieDtoConverter tmdbMovieToMovieDto;

  @GetMapping("/api/upcoming")
  @ResponseBody
  public List<TmdbMovie> getUpcoming() {
    return tmdbService.getUpcomingMovies(1);
  }

  @GetMapping("/")
  public String showLandingPage(Model model) {
    model.addAttribute("pageTitle", "Landing");
    model.addAttribute(
        "movies",
        tmdbService.getUpcomingMovies(1).stream().map(tmdbMovieToMovieDto::convert).toList());
    return "landing";
  }

    @GetMapping("/home")
    public String showHomePage(Model model) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute(
                "movies",
                tmdbService.getUpcomingMovies(1).stream().map(tmdbMovieToMovieDto::convert).toList());
        return "landing";
    }
}
