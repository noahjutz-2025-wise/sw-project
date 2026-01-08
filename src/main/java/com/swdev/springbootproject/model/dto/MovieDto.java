package com.swdev.springbootproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MovieDto {
  private final long id;
  private final String title;
  private final String posterPath;
  private final String backdropPath;
  private final String overview;
  private final MovieDtoType type;
}
