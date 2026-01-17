package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.*;
import com.swdev.springbootproject.service.TMDBService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class FeedController {
  private @NonNull StringToJsonNodesConverter stringToJsonNodes;
  private @NonNull JsonNodeToMediaDtoConverter jsonNodeToMedia;
  private @NonNull TMDBService tmdbService;
  private @NonNull TmdbTvToMediaDtoConverter tvToMedia;
  private @NonNull TmdbMovieToMediaDtoConverter movieToMedia;

  @GetMapping("/app/feed")
  public String showFeed(Model model) {
    model.addAttribute("pageTitle", "Feed");
    return "feed";
  }

  @GetMapping("/app/feed/mediaCards")
  public String showFeedMediaCards(@RequestParam("media_json") String medias, Model model) {
    final var mediaDtos =
        Objects.requireNonNull(stringToJsonNodes.convert(medias)).stream()
            .map(jsonNodeToMedia::convert)
            .filter(Objects::nonNull)
            .map(
                it ->
                    switch (it.getType()) {
                      case MOVIE -> movieToMedia.convert(tmdbService.getMovieDetails(it.getId()));
                      case TV -> tvToMedia.convert(tmdbService.getTvDetails(it.getId()));
                    })
            .toList();
    model.addAttribute("movies", mediaDtos);
    return "fragments/movie_card_row::movie_card_row";
  }
}
