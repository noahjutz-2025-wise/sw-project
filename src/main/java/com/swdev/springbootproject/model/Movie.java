package com.swdev.springbootproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
  public String getTitle() {
    return title;
  }

  public String getPosterPath() {
    return posterPath;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setPosterPath(String posterUrl) {
    this.posterPath = posterUrl;
  }

  private String title;
  @JsonProperty("poster_path")
  private String posterPath;

  public String getPosterUrl() {
    String posterBaseUrl = "https://image.tmdb.org/t/p/w342";
    return posterBaseUrl + posterPath;
  }
}
