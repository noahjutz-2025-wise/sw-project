package com.swdev.springbootproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RecommendationController {

    @GetMapping("/api/recommendations")
    public Map<String, Object> getRecommendations(@RequestParam(defaultValue = "any") String mood) {
        return Map.of(
                "mood", mood,
                "recommendations", List.of("Wicked: For Good", "The Fantastic Four: First Steps", "Paddington")
        );
    }
}
