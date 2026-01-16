package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.model.dto.MediaDtoType;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbMovieToMovieDtoConverter implements Converter<TmdbMovie, MediaDto> {

  @Override
  public @Nullable MediaDto convert(TmdbMovie source) {
    return MediaDto.builder()
        .id(source.getId())
        .title(source.getTitle())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .releaseDate(source.getReleaseDate())
        .overview(source.getOverview())
        .type(MediaDtoType.MOVIE)
        .build();
  }
}
