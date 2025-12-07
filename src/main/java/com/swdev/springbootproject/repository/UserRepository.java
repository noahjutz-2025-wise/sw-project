package com.swdev.springbootproject.repository;

import java.util.Optional;
import com.swdev.springbootproject.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {

  Optional<User> findByVerificationToken(String token);

  boolean existsByEmail(String email);
}