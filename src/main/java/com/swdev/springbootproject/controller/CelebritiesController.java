package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.TmdbPersonToPersonDtoConverter;
import com.swdev.springbootproject.model.dto.PersonDto;
import com.swdev.springbootproject.model.tmdb.TmdbPerson;
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
public class CelebritiesController {

  private final TMDBService tmdbService;
  private final TmdbPersonToPersonDtoConverter tmdbPersonToPersonDto;

  @GetMapping("/api/celebrities")
  @ResponseBody
  public List<TmdbPerson> getCelebrities(@RequestParam(defaultValue = "1") int page) {
    if (page > 30) {
      page = 30;
    }
    return tmdbService.getPopularPeople(page);
  }

  @GetMapping("/celebrities")
  public String showCelebrities(@RequestParam(defaultValue = "1") int page, Model model) {
    if (page > 30) {
      page = 30;
    }
    List<PersonDto> people =
        tmdbService.getPopularPeople(page).stream().map(tmdbPersonToPersonDto::convert).toList();

    model.addAttribute("pageTitle", "Celebrities");
    model.addAttribute("people", people);
    model.addAttribute("currentPage", page);
    return "celebrities";
  }
}
