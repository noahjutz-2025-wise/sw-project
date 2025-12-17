package com.swdev.springbootproject.service;

import com.swdev.springbootproject.repository.CbUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CbUserDetailsService implements UserDetailsService {

  private final CbUserRepository userRepository;

  @Override
  @NonNull
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    System.out.println("loadByUsername CALLED ============");
    return userRepository
        .findByEmail(username)
        .orElseThrow(() -> new UsernameNotFoundException(username));
  }
}
