package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.entity.Movie;
import com.swdev.springbootproject.entity.MovieBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieBookmarkRepository extends JpaRepository<MovieBookmark, Long> {
    Optional<MovieBookmark> findByUserAndMovie(CbUser user, Movie movie);
}
