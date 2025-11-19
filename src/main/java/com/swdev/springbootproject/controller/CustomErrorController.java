package com.swdev.springbootproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

  @RequestMapping("/error")
  public String handleError(HttpServletRequest request, Model model) {
    Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
    String errorMessage = (String) request.getAttribute("jakarta.servlet.error.message");

    model.addAttribute("statusCode", statusCode);
    model.addAttribute("errorMessage", errorMessage);

    return "error"; // return custom error view
  }

  public String getErrorPath() {
    return "/error";
  }
}
