package com.swdev.springbootproject.model.tmdb;

import java.util.List;
import lombok.Getter;

@Getter
public class PaginatedResults<T> {
  private List<T> results;
}
