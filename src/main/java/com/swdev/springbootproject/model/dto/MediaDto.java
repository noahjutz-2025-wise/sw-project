package com.swdev.springbootproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaDto {
  private long id;
  private String title;
  private String posterPath;
  private String backdropPath;
  private String releaseDate;
  private String overview;
  private MediaDtoType type;
  private Boolean certifiedBanger;
  private String tagline;
  private Long revenue;
  private Integer runtime;
  private Float popularity;
  private Float voteAverage;
  private Integer voteCount;
}
