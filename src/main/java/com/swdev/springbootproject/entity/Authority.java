package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "authorities")
public class Authority {

  @EmbeddedId private AuthorityId id;
}

@Data
@Embeddable
class AuthorityId {
  private String username;
  private String authority;
}
