package com.swdev.springbootproject.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class PostDto {
  private final String content;
  private final List<MediaDto> media;
  private final String authorName;
  private final String authorEmail;
  private final long authorId;
}
