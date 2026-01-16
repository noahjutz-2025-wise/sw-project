package com.swdev.springbootproject.component;

import com.swdev.springbootproject.model.dto.MediaDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@RequiredArgsConstructor
@Component
public class StringToMediaDtoListConverter implements Converter<String, List<MediaDto>> {
  private @NonNull ObjectMapper objectMapper;

  @Override
  public @Nullable List<MediaDto> convert(String source) {
    return objectMapper.readValue(source, new TypeReference<>() {});
  }
}
