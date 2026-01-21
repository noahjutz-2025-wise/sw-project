package com.swdev.springbootproject.controller_rest;

import com.swdev.springbootproject.component.PostToPostDtoConverter;
import com.swdev.springbootproject.entity.Post;
import com.swdev.springbootproject.model.dto.PostDto;
import com.swdev.springbootproject.repository.PostRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/api/posts")
@ResponseBody
public class PostController {
  private final PostRepository postRepository;
  private final PostToPostDtoConverter postToPostDtoConverter;

  public PostController(
      PostRepository postRepository, PostToPostDtoConverter postToPostDtoConverter) {
    this.postRepository = postRepository;
    this.postToPostDtoConverter = postToPostDtoConverter;
  }

  @GetMapping
  public List<PostDto> posts(@RequestParam(defaultValue = "0") int page) {
    return postRepository
        .findAll(Pageable.ofSize(10).withPage(page))
        .map(postToPostDtoConverter::convert)
        .stream()
        .toList();
  }
}
