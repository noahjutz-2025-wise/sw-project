package com.swdev.springbootproject.model;

import java.util.List;

public class TMDBApiResponse {
    public List<Movie> getResults() {
        return results;
    }

    private List<Movie> results;
}
