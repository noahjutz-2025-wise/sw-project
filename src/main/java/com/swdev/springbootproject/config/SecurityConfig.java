package com.swdev.springbootproject.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {
  private final String ownerPassword;

  public SecurityConfig(@Value("${owner_password}") String ownerPassword) {
    this.ownerPassword = ownerPassword;
  }

  private final PasswordEncoder enc = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/app/**")
                    .authenticated()
                    .requestMatchers("/signup", "/", "/css/**", "/js/**", "/error", "/webjars/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .build();
  }

  @Bean
  UserDetailsService auth(DataSource ds) {
    final var manager = new JdbcUserDetailsManager(ds);
    if (ownerPassword != null && !ownerPassword.isEmpty() && !manager.userExists("admin")) {
      final var admin =
          User.builder()
              .username("admin")
              .password(enc.encode(ownerPassword))
              .roles("OWNER")
              .build();
      manager.createUser(admin);
    }

    return manager;
  }
}
