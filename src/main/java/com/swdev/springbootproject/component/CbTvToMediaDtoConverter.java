package com.swdev.springbootproject.component;

import com.swdev.springbootproject.entity.CbTv;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.service.TMDBService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CbTvToMediaDtoConverter implements Converter<CbTv, MediaDto> {
  private @NonNull TMDBService tmdbService;
  private @NonNull TmdbTvToMediaDtoConverter tmdbTvToMediaDto;

  @Override
  public @Nullable MediaDto convert(CbTv source) {
    final var tv = tmdbService.getTvDetails(source.getId());
    return tmdbTvToMediaDto.convert(tv);
  }
}
