package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.dto.MovieDto;
import com.swdev.springbootproject.model.dto.MovieDtoType;
import com.swdev.springbootproject.model.tmdb.TmdbMovie;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbMovieToMovieDtoConverter implements Converter<TmdbMovie, MovieDto> {

  @Override
  public @Nullable MovieDto convert(TmdbMovie source) {
    return MovieDto.builder()
        .id(source.getId())
        .title(source.getTitle())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .releaseDate(source.getReleaseDate())
        .overview(source.getOverview())
        .type(MovieDtoType.MOVIE)
        .build();
  }
}
