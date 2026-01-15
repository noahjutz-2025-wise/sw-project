package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.model.dto.UserDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class SignupController {

  private final PasswordEncoder enc;
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  private final EmailService emailService;
  private final CbUserRepository cbUserRepository;

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {
      return "redirect:/home";
    }

    model.addAttribute("user", new CbUser());
    return "signup";
  }

  @PostMapping("/signup")
  public String processSignup(
      @ModelAttribute("user") UserDto userDto,
      Model model,
      HttpServletRequest request,
      HttpServletResponse response) {

    if (cbUserRepository.existsByEmail(userDto.getEmail())) {
      model.addAttribute("error", "Email already exists. Please use a different email.");
      return "signup";
    }

    final var insertedUser =
        cbUserRepository.save(
            CbUser.builder()
                .email(userDto.getEmail())
                .password(enc.encode(userDto.getPassword()))
                .name(userDto.getName())
                .build());

    securityContextRepository.saveContext(
        createAuth(userDto.getEmail(), userDto.getPassword()), request, response);

    emailService.sendVerificationEmail(insertedUser);
    return "redirect:/home";
  }

  private SecurityContext createAuth(String username, String password) {
    final var authReq = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    final var authRes = authenticationManager.authenticate(authReq);
    final var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authRes);
    return context;
  }
}
