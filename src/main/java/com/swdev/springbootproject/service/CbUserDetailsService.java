package com.swdev.springbootproject.service;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.CbUserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CbUserDetailsService implements UserDetailsService {

  private final CbUserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    return cbUserToUserDetails(userRepository.findByEmail(username));
  }

  private UserDetails cbUserToUserDetails(CbUser cbUser) {
    return User.builder().username(cbUser.getEmail()).password(cbUser.getPassword()).build();
  }
}
