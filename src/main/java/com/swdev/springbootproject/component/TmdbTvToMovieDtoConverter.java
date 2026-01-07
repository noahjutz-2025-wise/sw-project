package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.dto.MovieDto;
import com.swdev.springbootproject.model.dto.MovieDtoType;
import com.swdev.springbootproject.model.tmdb.TmdbTv;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbTvToMovieDtoConverter implements Converter<TmdbTv, MovieDto> {

  @Override
  public @Nullable MovieDto convert(TmdbTv source) {
    return MovieDto.builder()
        .id(source.getId())
        .title(source.getName())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .overview(source.getOverview())
        .type(MovieDtoType.TV)
        .build();
  }
}
