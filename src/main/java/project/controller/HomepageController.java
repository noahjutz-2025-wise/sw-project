package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class HomepageController {

    @GetMapping("/homepage")
    public String getHome() {
        return "homepage";
    }

    @PostMapping("/error_404")
    public String error_404() {
        return "error_404";
    }

}