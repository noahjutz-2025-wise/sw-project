package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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

  @OneToMany(mappedBy = "author")
  private List<Post> posts;

  @Column(nullable = false, columnDefinition = "boolean default false")
  @Builder.Default
  private boolean certifiedCritic = false;

  @Override
  @NonNull
  public Collection<? extends GrantedAuthority> getAuthorities() {
    Set<GrantedAuthority> auths = new HashSet<>();
    auths.add(new SimpleGrantedAuthority(authority));
    if (certifiedCritic) {
      auths.add(new SimpleGrantedAuthority("ROLE_CRITIC"));
    }
    return auths;
  }

  @Override
  @NonNull
  public String getUsername() {
    return email;
  }
}
