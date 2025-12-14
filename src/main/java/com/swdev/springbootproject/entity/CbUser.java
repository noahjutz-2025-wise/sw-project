package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class CbUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;

  @Column(name = "username", nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean enabled = true;

  @Column(nullable = false, columnDefinition="boolean default false")
  private boolean verified = false;
}
