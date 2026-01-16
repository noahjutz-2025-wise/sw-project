package com.swdev.springbootproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FeedController {

    @GetMapping("/app/feed")
    public String showFeed(Model model) {
        model.addAttribute("pageTitle", "Feed");
        return "feed";
    }
}
