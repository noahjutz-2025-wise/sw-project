package com.swdev.springbootproject.component.converter;

import com.swdev.springbootproject.model.dto.PersonDto;
import com.swdev.springbootproject.model.tmdb.TmdbPerson;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TmdbPersonToPersonDtoConverter implements Converter<TmdbPerson, PersonDto> {

  @Override
  public @Nullable PersonDto convert(TmdbPerson source) {
    return PersonDto.builder()
        .id(source.getId())
        .name(source.getName())
        .birthday(source.getBirthday())
        .placeOfBirth(source.getPlaceOfBirth())
        .biography(source.getBiography())
        .knownForDepartment(source.getKnownForDepartment())
        .popularity(source.getPopularity())
        .profilePath(source.getProfilePath())
        .build();
  }
}
