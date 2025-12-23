package com.swdev.springbootproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/movie")
public class MovieController {
  @GetMapping("/{id}")
  public String movie(@PathVariable int id, Model model) {
    model.addAttribute(id);
    return "movie_details";
  }
}
