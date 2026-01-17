package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.*;
import com.swdev.springbootproject.entity.*;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.repository.*;
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
  private final @NonNull StringToJsonNodesConverter stringToJsonNodes;
  private final @NonNull JsonNodeToMediaDtoConverter jsonNodeToMedia;
  private final @NonNull TMDBService tmdbService;
  private final @NonNull TmdbTvToMediaDtoConverter tvToMedia;
  private final @NonNull TmdbMovieToMediaDtoConverter movieToMedia;

  private final @NonNull PostRepository postRepository;
  private final @NonNull MovieRepository movieRepository;
  private final @NonNull TvRepository tvRepository;
  private final @NonNull PostToCbMovieRepository postToCbMovieRepository;
  private final @NonNull PostToCbTvRepository postToCbTvRepository;

  @GetMapping()
  public String showFeed(Model model) {
    model.addAttribute("pageTitle", "Feed");

    final var posts =
        postRepository.findAll().stream()
            .map(
                post -> {
                  final var movies =
                      postToCbMovieRepository.findAllByPost(post).stream()
                          .map(it -> movieToMedia.convert(tmdbService.getMovieDetails(it.getMovie().getId())))
                          .toList();
                  final var tvs =
                      postToCbTvRepository.findAllByPost(post).stream()
                          .map(it -> tvToMedia.convert(tmdbService.getTvDetails(it.getTv().getId())))
                          .toList();

                  final var media = new java.util.ArrayList<MediaDto>();
                  media.addAll(movies);
                  media.addAll(tvs);

                  return new PostDto(post.getContent(), media);
                })
            .toList();

    model.addAttribute("posts", posts);
    return "feed";
  }

  public record PostDto(String content, List<MediaDto> media) {}

  @GetMapping("/mediaCards")
  public String showFeedMediaCards(@RequestParam("media_json") String medias, Model model) {
    final var mediaDtos = stringToMediaDtos(medias);
    model.addAttribute("movies", mediaDtos);
    return "fragments/movie_card_row::movie_card_row";
  }

  @PostMapping()
  public String post(
      @RequestParam("media_json") String medias,
      @RequestParam("post-text") String postText,
      Model model) {
    final var mediaDtos = stringToMediaDtos(medias);

    final var post = postRepository.save(new Post(postText));

    for (final var mediaDto : mediaDtos) {
      switch (mediaDto.getType()) {
        case MOVIE -> {
          final var movie =
              movieRepository
                  .findById(mediaDto.getId())
                  .orElseGet(() -> movieRepository.save(new CbMovie(mediaDto.getId())));
          postToCbMovieRepository.save(new PostToCbMovie(post, movie));
        }
        case TV -> {
          final var tv =
              tvRepository
                  .findById(mediaDto.getId())
                  .orElseGet(() -> tvRepository.save(new CbTv(mediaDto.getId())));
          postToCbTvRepository.save(new PostToCbTv(post, tv));
        }
      }
    }

    return "redirect:/app/feed";
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
