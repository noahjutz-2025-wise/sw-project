package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.EmailVerification;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationRepository
    extends JpaRepository<@NonNull EmailVerification, @NonNull Long> {

  Optional<EmailVerification> findByVerificationToken(String token);
}
