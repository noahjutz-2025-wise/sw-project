package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.User;
import com.swdev.springbootproject.repository.UserRepository;
import com.swdev.springbootproject.service.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupController {

  private final UserRepository userRepository;

  private final EmailService emailService;

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/signup")
  public String processSignup(
      @ModelAttribute("user") User user, Model model, RedirectAttributes redirectAttributes) {
    try {
      if (userRepository.existsByEmail(user.getEmail())) {
        model.addAttribute("error", "Email already exists. Please use a different email.");
        return "signup";
      }

      register(user);

      redirectAttributes.addFlashAttribute(
          "success", "Registration successful! Welcome, " + user.getName() + "!");

      return "redirect:/signup-success";
    } catch (Exception e) {
      System.out.println(e.getMessage());
      model.addAttribute("error", "Registration failed. Please try again.");
      return "signup";
    }
  }

  @GetMapping("/signup-success")
  public String showSuccessPage() {
    return "signup-success";
  }

  public void register(User user) {
    String token = UUID.randomUUID().toString();
    user.setVerificationToken(token);
    user.setTokenExpiryDate(LocalDateTime.now().plusMinutes(30));
    userRepository.save(user);

    emailService.sendVerificationEmail(user.getEmail(), token);
  }
}
