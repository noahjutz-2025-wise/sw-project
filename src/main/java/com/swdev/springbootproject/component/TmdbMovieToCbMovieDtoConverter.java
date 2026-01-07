package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.CbMovieDto;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbMovieToCbMovieDtoConverter implements Converter<TmdbMovie, CbMovieDto> {

  @Override
  public @Nullable CbMovieDto convert(TmdbMovie source) {
    return CbMovieDto.builder()
        .id(source.getId())
        .title(source.getTitle())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .overview(source.getOverview())
        .build();
  }
}
