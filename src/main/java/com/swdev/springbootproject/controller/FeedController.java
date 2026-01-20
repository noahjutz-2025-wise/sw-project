package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.*;
import com.swdev.springbootproject.entity.*;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.model.dto.PostDto;
import com.swdev.springbootproject.model.dto.UserDto;
import com.swdev.springbootproject.repository.*;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
  private final @NonNull PostToPostDtoConverter postToPostDto;
  private final @NonNull PostDtoToPostConverter postDtoToPost;

  private final @NonNull PostRepository postRepository;
  private final @NonNull MovieRepository movieRepository;
  private final @NonNull TvRepository tvRepository;
  private final @NonNull PostToCbMovieRepository postToCbMovieRepository;
  private final @NonNull PostToCbTvRepository postToCbTvRepository;
  private final @NonNull CbUserRepository cbUserRepository;

  @GetMapping()
  public String showFeed(Model model) {
    model.addAttribute("pageTitle", "Feed");

    final var posts =
        postRepository.findAll(PageRequest.of(0, 10, Sort.by("id").descending())).stream()
            .map(postToPostDto::convert)
            .toList();

    model.addAttribute("posts", posts);
    return "feed";
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Transactional
  public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
    postRepository.deleteById(id);
    return ResponseEntity.ok().header("HX-Refresh", "true").build();
  }

  @GetMapping("/mediaCards")
  public String showFeedMediaCards(@RequestParam("media_json") String medias, Model model) {
    final var mediaDtos = stringToMediaDtos(medias);
    model.addAttribute("movies", mediaDtos);
    return "fragments/movie_card_row::movie_card_row";
  }

  @PostMapping()
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Void> post(
      @RequestParam("media_json") String medias,
      @RequestParam("post-text") String postText,
      Model model,
      Authentication authentication) {
    final var mediaDtos = stringToMediaDtos(medias);
    final var user = cbUserRepository.findByEmail(authentication.getName()).orElseThrow();
    final var userDto = UserDto.builder().id(user.getId()).build();

    final var postDto =
        PostDto.builder().content(postText).media(mediaDtos).author(userDto).build();

    final var post = postDtoToPost.convert(postDto);

    postRepository.save(post);

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

    return ResponseEntity.ok().header("HX-Refresh", "true").build();
  }

  @GetMapping("/delete-confirm/{id}")
  public String deleteConfirm(@PathVariable Long id, Model model) {
    final var post = postRepository.findById(id).orElseThrow();
    model.addAttribute("post", post);
    return "feed::delete_post_confirm_dialog";
  }

  @GetMapping("edit-dialog/{id}")
  public String getEditDialog(@PathVariable Long id, Model model) {
    final var post = postRepository.findById(id).orElseThrow();
    model.addAttribute("postDto", postToPostDto.convert(post));
    return "feed::postModal";
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
