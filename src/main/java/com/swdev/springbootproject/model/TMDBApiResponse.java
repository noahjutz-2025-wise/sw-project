package com.swdev.springbootproject.model;

import java.util.List;
import lombok.Getter;

@Getter
public class TMDBApiResponse {
  private List<Movie> results;
}
