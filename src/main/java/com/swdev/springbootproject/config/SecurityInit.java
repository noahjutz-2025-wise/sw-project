package com.swdev.springbootproject.config;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.CbUserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SecurityInit implements ApplicationRunner {
  public static final String DEFAULT_OWNER_USERNAME = "admin";
  public static final String DEFAULT_OWNER_ROLE = "admin";

  private final PasswordEncoder enc;
  private final String defaultOwnerUserPassword;
  private final CbUserRepository userRepository;

  public SecurityInit(
      PasswordEncoder enc,
      CbUserRepository userRepository,
      @Value("${security.owner.default_password:#{null}}") String defaultOwnerUserPassword) {
    this.enc = enc;
    this.defaultOwnerUserPassword = defaultOwnerUserPassword;
    this.userRepository = userRepository;
  }

  @Override
  public void run(@NonNull ApplicationArguments args) {
    createOwner();
  }

  private void createOwner() {
    if (defaultOwnerUserPassword != null
        && !defaultOwnerUserPassword.isEmpty()
        && !userRepository.existsByEmail(DEFAULT_OWNER_USERNAME)) {
      userRepository.save(
          CbUser.builder()
              .email(DEFAULT_OWNER_USERNAME)
              .password(enc.encode(defaultOwnerUserPassword))
              .authority(DEFAULT_OWNER_ROLE)
              .build());
    }
  }
}
