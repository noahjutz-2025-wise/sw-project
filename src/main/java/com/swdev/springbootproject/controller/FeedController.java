package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.component.StringToMediaDtoListConverter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class FeedController {
    private @NonNull StringToMediaDtoListConverter stringToMedia;

    @GetMapping("/app/feed")
    public String showFeed(Model model) {
        model.addAttribute("pageTitle", "Feed");
        return "feed";
    }

    @GetMapping("/app/feed/mediaCards")
    @ResponseBody
    public String showFeedMediaCards(@RequestParam("media_json") String medias, Model model) {
        return stringToMedia.convert(medias).toString();
    }
}
