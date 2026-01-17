package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.*;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.service.TMDBService;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/feed")
@RequiredArgsConstructor
public class FeedController {
  private @NonNull StringToJsonNodesConverter stringToJsonNodes;
  private @NonNull JsonNodeToMediaDtoConverter jsonNodeToMedia;
  private @NonNull TMDBService tmdbService;
  private @NonNull TmdbTvToMediaDtoConverter tvToMedia;
  private @NonNull TmdbMovieToMediaDtoConverter movieToMedia;

  @GetMapping()
  public String showFeed(Model model) {
    model.addAttribute("pageTitle", "Feed");
    return "feed";
  }

  @GetMapping("/mediaCards")
  public String showFeedMediaCards(@RequestParam("media_json") String medias, Model model) {
    final var mediaDtos = stringToMediaDtos(medias);
    model.addAttribute("movies", mediaDtos);
    return "fragments/movie_card_row::movie_card_row";
  }

  @PostMapping()
  @ResponseBody
  public String post(
      @RequestParam("media_json") String medias,
      @RequestParam("post-text") String postText,
      Model model) {
    return stringToMediaDtos(medias).toString() + postText;
  }

  private List<MediaDto> stringToMediaDtos(String medias) {
    return Objects.requireNonNull(stringToJsonNodes.convert(medias)).stream()
        .map(jsonNodeToMedia::convert)
        .filter(Objects::nonNull)
        .map(
            it ->
                switch (it.getType()) {
                  case MOVIE -> movieToMedia.convert(tmdbService.getMovieDetails(it.getId()));
                  case TV -> tvToMedia.convert(tmdbService.getTvDetails(it.getId()));
                })
        .toList();
  }
}
