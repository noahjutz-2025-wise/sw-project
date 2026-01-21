package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
  Page<Post> findAllByMovies(CbMovie movie, Pageable pageable);

  Page<Post> findAllByMovies_Id(Long movieId, Pageable pageable);

  Page<Post> findAllByTvs_Id(Long tvId, Pageable pageable);
}
