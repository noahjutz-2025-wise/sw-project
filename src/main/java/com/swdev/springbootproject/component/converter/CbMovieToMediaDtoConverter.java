package com.swdev.springbootproject.component.converter;

import com.swdev.springbootproject.entity.CbMovie;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CbMovieToMediaDtoConverter implements Converter<CbMovie, MediaDto> {
  private @NonNull TMDBService tmdbService;
  private @NonNull TmdbMovieToMediaDtoConverter tmdbMovieToMediaDto;

  @Override
  public @Nullable MediaDto convert(CbMovie source) {
    final var movie = tmdbService.getMovieDetails(source.getId());
    return tmdbMovieToMediaDto.convert(movie);
  }
}
