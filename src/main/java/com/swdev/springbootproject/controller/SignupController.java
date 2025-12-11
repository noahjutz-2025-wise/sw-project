package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.CbUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class SignupController {

  private final CbUserRepository userRepository;

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    model.addAttribute("user", new CbUser());
    return "signup";
  }

  @PostMapping("/signup")
  public String processSignup(
      @ModelAttribute("user") CbUser cbUser,
      PasswordEncoder enc,
      UserDetailsManager manager,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (manager.userExists(cbUser.getEmail())) {
      model.addAttribute("error", "Email already exists. Please use a different email.");
      return "signup";
    }

    final var userDetails =
        User.builder()
            .username(cbUser.getEmail())
            .passwordEncoder(enc::encode)
            .password(cbUser.getPassword())
            .build();

    manager.createUser(userDetails);

    redirectAttributes.addFlashAttribute(
        "success", "Registration successful! Welcome, " + cbUser.getName() + "!");

    return "redirect:/signup-success";
  }

  @GetMapping("/signup-success")
  public String showSuccessPage() {
    return "signup-success";
  }
}
