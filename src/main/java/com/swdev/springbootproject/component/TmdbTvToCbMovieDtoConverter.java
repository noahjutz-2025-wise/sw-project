package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.dto.CbMovieDto;
import com.swdev.springbootproject.model.tmdb.TmdbTv;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbTvToCbMovieDtoConverter implements Converter<TmdbTv, CbMovieDto> {

  @Override
  public @Nullable CbMovieDto convert(TmdbTv source) {
    return CbMovieDto.builder()
        .id(source.getId())
        .title(source.getName())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .overview(source.getOverview())
        .build();
  }
}
