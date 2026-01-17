package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbMovieToMediaDtoConverter;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import com.swdev.springbootproject.service.CertifiedBangerService;
import com.swdev.springbootproject.service.TMDBService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MoodController {
  private final TMDBService tmdbService;
  private final TmdbMovieToMediaDtoConverter tmdbMovieToMovieDto;
  private final CertifiedBangerService certifiedBangerService;

  @GetMapping("/api/mood")
  public List<TmdbMovie> getMood(
      @RequestParam(required = false) String mood, @RequestParam(defaultValue = "1") int page) {
    return tmdbService.getMoviesByMood(mood, page);
  }

  @GetMapping("/mood")
  public String showMoviesByMood(
      @RequestParam(name = "mood", required = false) String mood,
      @RequestParam(defaultValue = "1") int page,
      Model model) {

    if (mood == null || mood.isBlank()) {
      mood = "happy";
    }

    List<MediaDto> movies =
        tmdbService.getMoviesByMood(mood, page).stream().map(tmdbMovieToMovieDto::convert).toList();
    certifiedBangerService.applyCertifiedBangerFlag(movies);
    model.addAttribute("mood", mood);
    model.addAttribute("pageTitle", "Mood - Genres By Mood");
    model.addAttribute("movies", movies);

    model.addAttribute("currentPage", page);
    return "mood";
  }
}
