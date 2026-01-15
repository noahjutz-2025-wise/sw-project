package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CertifiedBanger;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertifiedBangerRepository
    extends JpaRepository<@NonNull CertifiedBanger, @NonNull Long> {
  boolean existsById(@NonNull Long id);
}
