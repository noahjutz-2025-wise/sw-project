package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.PostToPostDtoConverter;
import com.swdev.springbootproject.component.TmdbTvToMediaDtoConverter;
import com.swdev.springbootproject.repository.PostRepository;
import com.swdev.springbootproject.repository.TvRepository;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/app/tv")
@RequiredArgsConstructor
public class TvController {
  private final @NonNull TvRepository tvRepository;
  private final TMDBService tMDBService;
  private final TmdbTvToMediaDtoConverter tmdbTvToMediaDtoConverter;
  private final PostRepository postRepository;
  private final PostToPostDtoConverter postToPostDtoConverter;

  @GetMapping("/{id}")
  public String getTvDetails(@PathVariable Long id, Model model) {
    final var tv = tMDBService.getTvDetails(id);
    final var media = tmdbTvToMediaDtoConverter.convert(tv);
    final var posts =
        postRepository.findAllByTvs_Id(tv.getId(), Pageable.ofSize(10)).stream()
            .map(postToPostDtoConverter::convert)
            .toList();
    model.addAttribute("media", media);
    model.addAttribute("posts", posts);
    model.addAttribute("poster", TMDBService.POSTER_BASE_URL + tv.getPosterPath());
    model.addAttribute("backdrop", TMDBService.BACKDROP_BASE_URL + tv.getBackdropPath());
    return "movie_details";
  }
}
