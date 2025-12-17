package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbUser;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CbUserRepository extends JpaRepository<@NonNull CbUser, @NonNull Long> {
  boolean existsByEmail(String email);

  Optional<CbUser> findByEmail(String email);
}
