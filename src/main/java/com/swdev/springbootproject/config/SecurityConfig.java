package com.swdev.springbootproject.config;

import com.swdev.springbootproject.repository.CbUserRepository;
import com.swdev.springbootproject.service.CbUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      PasswordEncoder enc, UserDetailsService userDetailsService) {
    final var authProvider = new DaoAuthenticationProvider(userDetailsService);
    authProvider.setPasswordEncoder(enc);
    return new ProviderManager(authProvider);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/app/**", "/verify", "/user/profile")
                    .authenticated()
                    .requestMatchers("/signup", "/", "/css/**", "/js/**", "/error", "/webjars/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/mood").permitAll())
        .build();
  }

  @Bean
  UserDetailsService userDetailsService(CbUserRepository userRepository) {
    return new CbUserDetailsService(userRepository);
  }
}
