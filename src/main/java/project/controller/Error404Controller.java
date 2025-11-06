package project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class Error404Controller {

    @GetMapping("/error_404")
    public String error_404() {
        return "error_404";
    }

    @PostMapping("/homepage")
    public String getHome() {
        return "homepage";
    }

}
