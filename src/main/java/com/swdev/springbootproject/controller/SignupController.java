package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.User;
import com.swdev.springbootproject.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SignupController {

  private final UserRepository userRepository;

  public SignupController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

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

      userRepository.save(user);

      redirectAttributes.addFlashAttribute(
          "success", "Registration successful! Welcome, " + user.getFirstName() + "!");

      return "redirect:/signup-success";
    } catch (Exception e) {
      model.addAttribute("error", "Registration failed. Please try again.");
      return "signup";
    }
  }

  @GetMapping("/signup-success")
  public String showSuccessPage() {
    return "signup-success";
  }
}
