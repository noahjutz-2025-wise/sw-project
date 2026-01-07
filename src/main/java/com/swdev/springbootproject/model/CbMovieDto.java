package com.swdev.springbootproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CbMovieDto {
    private final long id;
    private final String title;
    private final String posterPath;
    private final String backdropPath;
    private final String overview;
}
