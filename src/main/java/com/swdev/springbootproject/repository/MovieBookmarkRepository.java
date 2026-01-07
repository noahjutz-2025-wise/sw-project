package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.MovieBookmark;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieBookmarkRepository extends JpaRepository<MovieBookmark, Long> {
  Optional<MovieBookmark> findByUserAndMovie(CbUser user, CbMovie cbMovie);
}
