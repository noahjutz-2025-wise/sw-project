package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/search")
class SearchController {
  private final TMDBService tmdbService;

  @GetMapping("")
  public String search() {
    return "search";
  }
}
