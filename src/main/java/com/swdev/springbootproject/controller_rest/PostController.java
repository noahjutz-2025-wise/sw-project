package com.swdev.springbootproject.controller_rest;

import com.swdev.springbootproject.component.PostSecurity;
import com.swdev.springbootproject.component.converter.PostDtoToPostConverter;
import com.swdev.springbootproject.component.converter.PostToPostDtoConverter;
import com.swdev.springbootproject.model.dto.PostDto;
import com.swdev.springbootproject.repository.PostRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@ResponseBody
public class PostController {
  private final PostRepository postRepository;
  private final PostToPostDtoConverter postToPostDtoConverter;
  private final PostDtoToPostConverter postDtoToPostConverter;
  private final PostSecurity postSecurity;

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  public List<PostDto> posts(@RequestParam(defaultValue = "0") int page) {
    return postRepository
        .findAll(Pageable.ofSize(10).withPage(page))
        .map(postToPostDtoConverter::convert)
        .stream()
        .toList();
  }

  @GetMapping("/{id}")
  public PostDto post(@PathVariable Long id) {
    return postToPostDtoConverter.convert(postRepository.findById(id).orElseThrow());
  }

  @PostMapping
  @PreAuthorize("isAuthenticated()")
  public PostDto createPost(@RequestBody PostDto postDto) {
    assert postDto.getId() == null;
    final var post = postDtoToPostConverter.convert(postDto);
    return postToPostDtoConverter.convert(postRepository.save(post));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("@postSecurity.isOwner(#id, authentication)")
  public void deletePost(@PathVariable Long id) {
    postRepository.deleteById(id);
  }

  @PutMapping
  @PreAuthorize("@postSecurity.isOwner(#postDto.id, authentication)")
  public PostDto updatePost(@RequestBody PostDto postDto) {
    assert postDto.getId() != null;
    final var post = postDtoToPostConverter.convert(postDto);
    return postToPostDtoConverter.convert(postRepository.save(post));
  }
}
