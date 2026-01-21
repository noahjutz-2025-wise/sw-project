package com.swdev.springbootproject.component;

import com.google.common.primitives.Longs;
import com.swdev.springbootproject.entity.CbUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtKeyLocator jwtKeyLocator;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    final var parser = Jwts.parser().keyLocator(jwtKeyLocator).build();
    final var token =
        Optional.ofNullable(request.getHeader("Authorization"))
            .filter(s -> s.startsWith("Bearer "))
            .map(s -> s.split(" ")[1])
            .map(parser::parseSignedClaims);

    final var jwtUserId = token.map(Jws::getPayload).map(Claims::getSubject).map(Longs::tryParse);

    if (jwtUserId.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    final var authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null) {
      filterChain.doFilter(request, response);
      return;
    }

    final var user = (CbUser) authentication.getPrincipal();
    final var authUserId = Optional.ofNullable(user).map(CbUser::getId);

    if (user == null || !authUserId.equals(jwtUserId)) {
      filterChain.doFilter(request, response);
      return;
    }

    final var authToken =
        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
    filterChain.doFilter(request, response);
  }
}
