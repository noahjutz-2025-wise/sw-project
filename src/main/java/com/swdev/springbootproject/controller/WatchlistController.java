package com.swdev.springbootproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app/watchlist")
class WatchlistController {
    @GetMapping
    public String watchlist() {
        return "watchlist";
    }
}
