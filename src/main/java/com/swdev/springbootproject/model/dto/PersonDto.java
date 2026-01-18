package com.swdev.springbootproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonDto {
  private final long id;
  private final String name;
  private final String birthday;
  private final String placeOfBirth;
  private final String biography;
  private final String knownForDepartment;
  private final float popularity;
  private final String profilePath;
}
