package com.swdev.springbootproject.component;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Component
public class StringToJsonNodesConverter implements Converter<String, List<JsonNode>> {
  private @NonNull ObjectMapper objectMapper;

  @Override
  public @Nullable List<JsonNode> convert(String source) {
    return objectMapper.readValue(source, new TypeReference<>() {});
  }
}
