package com.swdev.springbootproject.component;

import com.swdev.springbootproject.entity.Post;
import com.swdev.springbootproject.model.dto.PostDto;
import com.swdev.springbootproject.model.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class PostToPostDtoConverter implements Converter<Post, PostDto> {
  private @NonNull CbMovieToMediaDtoConverter cbMovieToMediaDto;
  private @NonNull CbTvToMediaDtoConverter cbTvToMediaDto;

  @Override
  public @Nullable PostDto convert(Post source) {
    final var movieMediaDtos = source.getMovies().stream().map(cbMovieToMediaDto::convert);
    final var tvMediaDtos = source.getTvs().stream().map(cbTvToMediaDto::convert);
    final var userDto = UserDto.builder().id(source.getAuthor().getId()).build();

    return PostDto.builder()
        .id(source.getId())
        .content(source.getContent())
        .media(Stream.concat(movieMediaDtos, tvMediaDtos).toList())
        .author(userDto)
        .build();
  }
}
