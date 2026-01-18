package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.Post;
import com.swdev.springbootproject.entity.PostToCbMovie;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostToCbMovieRepository
    extends JpaRepository<PostToCbMovie, PostToCbMovie.PostToCbMovieId> {
  List<PostToCbMovie> findAllByPost(Post post);
}
