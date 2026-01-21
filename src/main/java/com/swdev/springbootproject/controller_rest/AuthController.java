package com.swdev.springbootproject.controller_rest;

import com.swdev.springbootproject.component.JwtKeyLocator;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.model.dto.UserDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import io.jsonwebtoken.Jwts;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@ResponseBody
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final CbUserRepository cbUserRepository;
  private final JwtKeyLocator jwtKeyLocator;

  @PostMapping("")
  public String login(@RequestBody UserDto loginDto) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
    final var userId =
        cbUserRepository.findByEmail(loginDto.getEmail()).map(CbUser::getId).orElseThrow();
    return Jwts.builder()
        .subject(userId.toString())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plus(Duration.ofDays(1))))
        .signWith(jwtKeyLocator.locate(Jwts.header().build()))
        .compact();
  }
}
