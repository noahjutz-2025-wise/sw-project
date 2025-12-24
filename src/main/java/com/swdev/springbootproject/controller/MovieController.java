package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.repository.CbUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@RequestMapping("/app/movie")
public class MovieController {
  private CbUserRepository cbUserRepository;

  @GetMapping("/{id}")
  public String movie(@PathVariable int id, Model model) {
    model.addAttribute(id);
    return "movie_details";
  }

  // TODO make post mapping instead (add htmx)
  @GetMapping("/{id}/bookmark")
  public String bookmark(@PathVariable int id, @RequestParam String category) {
    // TODO insert to db
    return "movie_details";
  }
}
