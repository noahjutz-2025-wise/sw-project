package com.swdev.springbootproject.component;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.entity.CbTv;
import com.swdev.springbootproject.entity.Post;
import com.swdev.springbootproject.model.dto.PostDto;
import com.swdev.springbootproject.repository.CbUserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostDtoToPostConverter implements Converter<PostDto, Post> {
  private @NonNull CbUserRepository userRepository;

  @Override
  public @Nullable Post convert(PostDto source) {
    final var movies = new ArrayList<CbMovie>();
    final var tvs = new ArrayList<CbTv>();
    for (final var mediaDto : source.getMedia()) {
      switch (mediaDto.getType()) {
        case MOVIE -> movies.add(new CbMovie(mediaDto.getId()));
        case TV -> tvs.add(new CbTv(mediaDto.getId()));
      }
    }
    return Post.builder()
        .id(null)
        .content(source.getContent())
        .author(userRepository.findById(source.getAuthor().getId()).orElseThrow())
        .movies(movies)
        .tvs(tvs)
        .build();
  }
}
