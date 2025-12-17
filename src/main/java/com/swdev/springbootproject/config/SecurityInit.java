package com.swdev.springbootproject.config;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

@Component
public class SecurityInit implements ApplicationRunner {
  public static final String DEFAULT_OWNER_USERNAME = "admin";
  public static final String DEFAULT_OWNER_ROLE = "admin";

  private final UserDetailsManager userDetailsManager;
  private final PasswordEncoder enc;
  private final String defaultOwnerUserPassword;

  public SecurityInit(
      UserDetailsManager userDetailsManager,
      PasswordEncoder enc,
      @Value("${security.owner.default_password:#{null}}") String defaultOwnerUserPassword) {
    this.userDetailsManager = userDetailsManager;
    this.enc = enc;
    this.defaultOwnerUserPassword = defaultOwnerUserPassword;
  }

  @Override
  public void run(@NonNull ApplicationArguments args) {
    createOwner(userDetailsManager, enc, defaultOwnerUserPassword);
  }

  private void createOwner(
      UserDetailsManager manager, PasswordEncoder enc, String defaultOwnerUserPassword) {
    if (defaultOwnerUserPassword != null
        && !defaultOwnerUserPassword.isEmpty()
        && !manager.userExists(DEFAULT_OWNER_USERNAME)) {
      final var admin =
          User.builder()
              .username(DEFAULT_OWNER_USERNAME)
              .passwordEncoder(enc::encode)
              .password(defaultOwnerUserPassword)
              .roles(DEFAULT_OWNER_ROLE)
              .build();
      manager.createUser(admin);
    }
  }
}
