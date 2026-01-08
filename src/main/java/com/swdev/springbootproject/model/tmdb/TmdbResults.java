package com.swdev.springbootproject.model.tmdb;

import java.util.List;
import lombok.Getter;

@Getter
public class TmdbResults<T> {
  private List<T> results;
}
