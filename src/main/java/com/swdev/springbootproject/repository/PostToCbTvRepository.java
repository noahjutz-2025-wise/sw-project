package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.Post;
import com.swdev.springbootproject.entity.PostToCbTv;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostToCbTvRepository extends JpaRepository<PostToCbTv, PostToCbTv.PostToCbTvId> {
  List<PostToCbTv> findAllByPost(Post post);

  void deleteAllByPost(Post post);
}
