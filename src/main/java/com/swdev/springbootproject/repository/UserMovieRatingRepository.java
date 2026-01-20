package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.UserMovieRating;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMovieRatingRepository
    extends JpaRepository<@NonNull UserMovieRating, @NonNull Long> {
  Optional<UserMovieRating> findByUserAndMovie(CbUser user, CbMovie cbMovie);

  List<UserMovieRating> findByUser(CbUser user);
}
