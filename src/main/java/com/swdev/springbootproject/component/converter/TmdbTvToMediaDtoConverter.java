package com.swdev.springbootproject.component.converter;

import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.model.dto.MediaDtoType;
import com.swdev.springbootproject.model.tmdb.TmdbTv;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbTvToMediaDtoConverter implements Converter<TmdbTv, MediaDto> {

  @Override
  public @Nullable MediaDto convert(TmdbTv source) {
    return MediaDto.builder()
        .id(source.getId())
        .title(source.getName())
        .posterPath(source.getPosterPath())
        .backdropPath(source.getBackdropPath())
        .overview(source.getOverview())
        .releaseDate(source.getLastAirDate())
        .tagline(source.getTagline())
        .revenue(source.getRevenue())
        .popularity(source.getPopularity())
        .voteAverage(source.getVoteAverage())
        .voteCount(source.getVoteCount())
        .runtime(null)
        .type(MediaDtoType.TV)
        .certifiedBangerStatus(MediaDto.CertifiedBangerStatus.NOT_CERTIFIED)
        .build();
  }
}
