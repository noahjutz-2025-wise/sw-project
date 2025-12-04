package com.swdev.springbootproject.model;

import lombok.Getter;

import java.util.List;

@Getter
public class TMDBApiResponse {
  private List<Movie> results;
}
