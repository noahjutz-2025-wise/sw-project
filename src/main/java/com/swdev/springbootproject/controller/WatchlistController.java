package com.swdev.springbootproject.controller;
import com.swdev.springbootproject.component.TmdbMovieToMovieDtoConverter;
import com.swdev.springbootproject.entity.BookmarkStatus;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.MovieBookmarkRepository;
import com.swdev.springbootproject.service.TMDBService;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/watchlist")
class WatchlistController {

  private final MovieBookmarkRepository movieBookmarkRepository;
  private final TMDBService tmdbService;
  private final TmdbMovieToMovieDtoConverter tmdbMovieToMovieDtoConverter;

  @GetMapping
  public String watchlist() {
    return "watchlist";
  }

  @GetMapping("/results")
  public String results(
      @RequestParam(name = "search", defaultValue = "") String search,
      @RequestParam(name = "selectedTab", defaultValue = "watch-later") BookmarkStatus status,
      Authentication authentication,
      Model model) {
    CbUser user = (CbUser) authentication.getPrincipal();

    var movies =
        movieBookmarkRepository.findByUserAndStatus(user, status).stream()
            .map(bookmark -> tmdbService.getMovieDetails(bookmark.getMovie().getId()))
            .filter(Objects::nonNull)
            .filter(
                tmdbMovie ->
                    search.isBlank()
                        || tmdbMovie.getTitle().toLowerCase().contains(search.toLowerCase()))
            .map(tmdbMovieToMovieDtoConverter::convert)
            .collect(Collectors.toList());

    model.addAttribute("movies", movies);
    return "fragments/movie_card_grid :: movieCardGrid(movies=${movies})";
  }
}
