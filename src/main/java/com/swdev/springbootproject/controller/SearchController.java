package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.converter.TmdbMovieToMediaDtoConverter;
import com.swdev.springbootproject.component.converter.TmdbTvToMediaDtoConverter;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
  private final TmdbMovieToMediaDtoConverter movieToMedia;
  private final TmdbTvToMediaDtoConverter tvToMedia;
  private final CbUserRepository cbUserRepository;

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
                    .map(movieToMedia::convert)
                    .collect(Collectors.toList());
        model.addAttribute("movies", movies);
        yield "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
      }
      case "tv" -> {
        final var shows =
            search.isBlank()
                ? List.of()
                : tmdbService.searchTv(search).stream()
                    .map(tvToMedia::convert)
                    .collect(Collectors.toList());
        model.addAttribute("movies", shows);
        yield "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
      }
      case "users" -> {
        final var users =
            search.isBlank()
                ? List.<CbUser>of()
                : cbUserRepository.findByEmailContainingIgnoreCase(search.trim()).stream().filter(u -> !Objects.equals(u.getEmail(), "admin")).toList();
        model.addAttribute("users", users);
        yield "fragments/user_card_grid :: userCardGrid(users=${users})";
      }
      default -> {
        throw new IllegalArgumentException();
      }
    };
  }

  @GetMapping("/autocomplete")
  public String autocomplete(@RequestParam String query, Model model) {
    final List<MediaDto> movies =
        query.isBlank()
            ? List.of()
            : tmdbService.searchMovies(query).stream()
                .limit(5)
                .map(movieToMedia::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream()
                .toList();

    final List<MediaDto> tvs =
        query.isBlank()
            ? List.of()
            : tmdbService.searchTv(query).stream()
                .limit(5)
                .map(tvToMedia::convert)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())
                .stream()
                .toList();

    final var titles = Map.of("Movies", movies, "TV Shows", tvs);

    model.addAttribute("results", titles);
    return "fragments/dropdown_list::dropdown_list(results=${results})";
  }
}
