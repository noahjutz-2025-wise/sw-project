package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToCbMovieDtoConverter;
import com.swdev.springbootproject.component.TmdbTvToCbMovieDtoConverter;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import java.util.stream.Collectors;
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
  private final TmdbTvToCbMovieDtoConverter tmdbTvToCbMovieDto;
  private final TmdbMovieToCbMovieDtoConverter tmdbMovieToCbMovieDto;

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
      Model model) {
    return switch (searchType) {
      case "movies" -> {
        final var movies =
            search.isBlank()
                ? List.of()
                : tmdbService.searchMovies(search).stream()
                    .map(tmdbMovieToCbMovieDto::convert)
                    .collect(Collectors.toList());
        model.addAttribute("movies", movies);
        yield "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
      }
      case "tv" -> {
        final var shows =
            search.isBlank()
                ? List.of()
                : tmdbService.searchTv(search).stream()
                    .map(tmdbTvToCbMovieDto::convert)
                    .collect(Collectors.toList());
        model.addAttribute("movies", shows);
        yield "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
      }
      case "users" -> {
        throw new IllegalStateException("Not yet implemented");
      }
      default -> {
        throw new IllegalArgumentException();
      }
    };
  }
}
