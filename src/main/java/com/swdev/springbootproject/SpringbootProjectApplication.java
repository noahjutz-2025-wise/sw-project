package com.swdev.springbootproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class SpringbootProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringbootProjectApplication.class, args);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            a ->
                a.requestMatchers("/app/**")
                    .authenticated()
                    .requestMatchers(
                        "/signup",
                        "/",
                        "/css/**",
                        "/js/**",
                        "/error",
                        "/webjars/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .formLogin(Customizer.withDefaults())
        .build();
  }
}
