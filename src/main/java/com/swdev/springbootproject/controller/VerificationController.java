package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.repository.EmailVerificationRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/verify")
public class VerificationController {
  private final EmailVerificationRepository emailVerificationRepository;

  public VerificationController(EmailVerificationRepository emailVerificationRepository) {
    this.emailVerificationRepository = emailVerificationRepository;
  }

  @GetMapping
  public String verifyEmail(@RequestParam("token") String token, Model model) {

    return emailVerificationRepository
        .findByVerificationToken(token)
        .map(
            emailVerification -> {
              if (emailVerification.getTokenExpiryDate() == null
                  || emailVerification.getTokenExpiryDate().isBefore(LocalDateTime.now())) {
                model.addAttribute("title", "Token expired.");
                model.addAttribute("message", "Your verification link has expired.");
                return "verify";
              }
              emailVerification.setVerified(true);
              emailVerification.setVerificationToken(null);
              emailVerification.setTokenExpiryDate(null);
              emailVerificationRepository.save(emailVerification);

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
