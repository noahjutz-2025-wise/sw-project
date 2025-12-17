package com.swdev.springbootproject.controller;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.EmailVerification;
import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.repository.EmailVerificationRepository;
import com.swdev.springbootproject.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
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
  private final UserDetailsManager userDetailsManager;
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository =
      new HttpSessionSecurityContextRepository();

  private final EmailService emailService;
  private final CbUserRepository cbUserRepository;
  private final EmailVerificationRepository emailVerificationRepository;

  @GetMapping("/signup")
  public String showSignupForm(Model model) {
    model.addAttribute("user", new CbUser());
    return "signup";
  }

  @PostMapping("/signup")
  public String processSignup(
      @ModelAttribute("user") CbUser cbUser,
      Model model,
      HttpServletRequest request,
      HttpServletResponse response) {

    if (userDetailsManager.userExists(cbUser.getEmail())) {
      model.addAttribute("error", "Email already exists. Please use a different email.");
      return "signup";
    }

    userDetailsManager.createUser(createUserDetails(cbUser));
    securityContextRepository.saveContext(
        createAuth(cbUser.getEmail(), cbUser.getPassword()), request, response);
    sendVerificationEmail(cbUserRepository.findByEmail(cbUser.getEmail()));
    return "redirect:/mood";
  }

  private UserDetails createUserDetails(CbUser cbUser) {
    return User.builder()
        .username(cbUser.getEmail())
        .passwordEncoder(enc::encode)
        .password(cbUser.getPassword())
        .roles("USER")
        .build();
  }

  private SecurityContext createAuth(String username, String password) {
    final var authReq = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    final var authRes = authenticationManager.authenticate(authReq);
    final var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authRes);
    return context;
  }

  public void sendVerificationEmail(CbUser cbUser) {
    String token = UUID.randomUUID().toString();
    EmailVerification emailVerification =
        new EmailVerification(token, LocalDateTime.now().plusMinutes(30), cbUser);
    emailVerificationRepository.save(emailVerification);
    emailService.sendVerificationEmail(cbUser.getEmail(), token);
  }
}
