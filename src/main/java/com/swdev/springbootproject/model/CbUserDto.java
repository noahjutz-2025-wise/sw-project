package com.swdev.springbootproject.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CbUserDto {
    private final String name;
    private final String email;
    private final String password;
}
