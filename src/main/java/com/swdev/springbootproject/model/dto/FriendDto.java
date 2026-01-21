package com.swdev.springbootproject.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendDto {
  private Long id;
  private Long friendshipId;
  private String name;
  private String email;
}
