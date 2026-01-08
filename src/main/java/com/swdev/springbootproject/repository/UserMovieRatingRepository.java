package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.UserMovieRating;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRatingRepository
    extends JpaRepository<@NonNull UserMovieRating, @NonNull Long> {}
