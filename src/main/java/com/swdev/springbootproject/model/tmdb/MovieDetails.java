package com.swdev.springbootproject.model.tmdb;

import lombok.Data;

@Data
public class MovieDetails {
  private String id;
  private String title;
  private String tagline;
  private String overview;
  private String backdropPath;
  private String posterPath;
  private String releaseDate;
  private int revenue;
  private int runtime;
  private float popularity;
  private float voteAverage;
  private int voteCount;
}
