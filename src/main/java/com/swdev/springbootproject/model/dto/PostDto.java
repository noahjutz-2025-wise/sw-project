package com.swdev.springbootproject.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostDto {
  private final Long id;
  private final String content;
  private final List<MediaDto> media;
  private final UserDto author;
}
