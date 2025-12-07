package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.repository.UserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerificationController {
  private final UserRepository userRepository;

  public VerificationController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @GetMapping("/verify")
  public String verifyEmail(@RequestParam("token") String token, Model model) {

    return userRepository
        .findByVerificationToken(token)
        .map(
            user -> {
              if (user.getTokenExpiryDate() == null
                  || user.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
                model.addAttribute("title", "Token expired.");
                model.addAttribute("message", "Your verification link has expired.");
                return "verify";
              }
              user.setVerified(true);
              user.setVerificationToken(null);
              user.setTokenExpiryDate(null);
              userRepository.save(user);

              model.addAttribute("title", "Email Verified!");
              model.addAttribute("message", "Your email has been successfully verified.");
              return "verify";
            })
        .orElseGet(
            () -> {
              model.addAttribute("title", "Invalid Link.");
              model.addAttribute("message", "The verification link you used is invalid.");
              return "verify";
            });
  }
}
