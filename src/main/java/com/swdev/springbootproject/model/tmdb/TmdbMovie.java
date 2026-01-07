package com.swdev.springbootproject.model.tmdb;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbMovie {
  private long id;
  private String title;
  private String tagline;
  private String overview;
  private String backdropPath;
  private String posterPath;
  private String releaseDate;
  private long revenue;
  private int runtime;
  private float popularity;
  private float voteAverage;
  private int voteCount;
}
