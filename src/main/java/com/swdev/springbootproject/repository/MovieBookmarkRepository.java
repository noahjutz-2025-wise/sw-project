package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.MovieBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieBookmarkRepository extends JpaRepository<MovieBookmark, Long> {}
