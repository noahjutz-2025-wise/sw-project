package com.swdev.springbootproject.model.tmdb;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbTv {
  private long id;
  private String name;
  private String posterPath;
  private String backdropPath;
  private String overview;
  private String firstAirDate;
  private String lastAirDate;
}
