package com.swdev.springbootproject.config;

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

import javax.sql.DataSource;

@Configuration
class SecurityConfig {
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
    final var admin =
        User.builder().username("admin").password(enc.encode("admin")).roles("ADMIN").build();
    manager.createUser(admin);
    return manager;
  }
}
