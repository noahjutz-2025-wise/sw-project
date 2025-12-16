package com.swdev.springbootproject.config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
class SecurityConfig {
  private final String defaultOwnerUserPassword;

  public SecurityConfig(@Value("${security.owner.default_password:#{null}}") String ownerPassword) {
    this.defaultOwnerUserPassword = ownerPassword;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/app/**")
                    .authenticated()
                    .requestMatchers(
                        "/signup", "/", "/css/**", "/js/**", "/error", "/webjars/**", "/verify")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .build();
  }

  @Bean
  UserDetailsManager auth(DataSource ds, PasswordEncoder enc) {
    final var manager = new JdbcUserDetailsManager(ds);
    if (defaultOwnerUserPassword != null
        && !defaultOwnerUserPassword.isEmpty()
        && !manager.userExists("admin")) {
      final var admin =
          User.builder()
              .username("admin")
              .passwordEncoder(enc::encode)
              .password(defaultOwnerUserPassword)
              .roles("OWNER")
              .build();
      manager.createUser(admin);
    }

    return manager;
  }
}
