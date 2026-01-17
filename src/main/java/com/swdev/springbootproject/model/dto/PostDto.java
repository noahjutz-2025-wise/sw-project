package com.swdev.springbootproject.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private final String content;
    private final List<MediaDto> media;
}
