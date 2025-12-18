package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Set;
import lombok.*;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "users")
public class CbUser implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;

  @Column(name = "username", nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  @Builder.Default
  private boolean enabled = true;

  @Column(nullable = false)
  @Builder.Default
  private String authority = "ROLE_USER";

  @Column(nullable = false, columnDefinition = "boolean default false")
  @Builder.Default
  private boolean verified = false;

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Set.of(new SimpleGrantedAuthority(authority));
  }

  @Override
  @NonNull
  public String getUsername() {
    return email;
  }
}
