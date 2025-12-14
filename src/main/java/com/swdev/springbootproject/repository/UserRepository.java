package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.User;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Long> {

  boolean existsByEmail(String email);
}
