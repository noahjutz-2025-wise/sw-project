package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/app/person")
public class BiographyController {

  private final TMDBService tmdbService;

  @GetMapping("/{id}")
  public String person(@PathVariable Long id, Model model) {
    final var person = tmdbService.getPersonDetails(id);
    model.addAttribute("personDetails", person);
    model.addAttribute("poster", TMDBService.POSTER_BASE_URL + person.getProfilePath());
    return "star_biography";
  }
}
