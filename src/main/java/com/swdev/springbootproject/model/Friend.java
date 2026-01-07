package com.swdev.springbootproject.model;

import com.swdev.springbootproject.entity.CbUser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Friend {
  long friendshipId;
  CbUser friend;
}
