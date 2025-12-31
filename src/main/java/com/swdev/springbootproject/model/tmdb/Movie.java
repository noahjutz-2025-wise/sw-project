package com.swdev.springbootproject.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Movie {

  private String title;

  @JsonProperty("poster_path")
  private String posterPath;

  public String getPosterUrl() {
    String posterBaseUrl = "https://image.tmdb.org/t/p/w342";
    return posterBaseUrl + posterPath;
  }
}
