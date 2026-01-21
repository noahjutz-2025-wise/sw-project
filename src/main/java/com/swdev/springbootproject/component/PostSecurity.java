package com.swdev.springbootproject.component;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component("postSecurity")
public class PostSecurity {
  private final PostRepository postRepository;

  public boolean isOwner(Long postId, Authentication authentication) {
    final var currentUser = (CbUser) authentication.getPrincipal();
    assert currentUser != null;

    return postRepository
        .findById(postId)
        .map(post -> post.getAuthor().getId().equals(currentUser.getId()))
        .orElse(false);
  }
}
