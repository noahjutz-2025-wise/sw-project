package com.swdev.springbootproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
  private final Long id;
  private final String name;
  private final String email;
  private final String password;
}
