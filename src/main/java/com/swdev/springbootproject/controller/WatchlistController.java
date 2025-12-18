package com.swdev.springbootproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/app/watchlist")
class WatchlistController {
  @GetMapping
  public String watchlist(
      @RequestParam(required = false) String searchTerm,
      @RequestParam(required = false) String collection) {
    return "watchlist";
  }
}
