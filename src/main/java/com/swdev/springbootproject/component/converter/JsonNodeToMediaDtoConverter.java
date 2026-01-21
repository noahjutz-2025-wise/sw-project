package com.swdev.springbootproject.component.converter;

import com.swdev.springbootproject.model.dto.MediaDto;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class JsonNodeToMediaDtoConverter implements Converter<JsonNode, MediaDto> {
  private @NonNull ObjectMapper objectMapper;

  @Override
  public @Nullable MediaDto convert(JsonNode source) {
    return objectMapper.convertValue(source, MediaDto.class);
  }
}
