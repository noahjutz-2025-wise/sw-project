package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.PostToCbMovie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostToCbMovieRepository
    extends JpaRepository<PostToCbMovie, PostToCbMovie.PostToCbMovieId> {}
