package com.swdev.springbootproject.model.tmdb;

import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.databind.annotation.JsonNaming;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TmdbPerson {
    private long id;
    private String name;
    private String birthday;
    private String placeOfBirth;
    private String biography;
    private String knownForDepartment;
    private float popularity;
    private String profilePath;
}
