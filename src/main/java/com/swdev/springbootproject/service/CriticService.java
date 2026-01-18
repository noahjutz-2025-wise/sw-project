package com.swdev.springbootproject.service;

import com.swdev.springbootproject.entity.CbUser;
import com.swdev.springbootproject.repository.CbUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CriticService {

  private final CbUserRepository cbUserRepository;

  public List<CbUser> findAllCbUsers() {
    return cbUserRepository.findAll();
  }

  @Transactional
  public CbUser updateCertifiedCriticStatus(Long userId, boolean certification) {
    CbUser cbUser = cbUserRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("User not found: " + userId));
    cbUser.setCertifiedCritic(certification);
    cbUserRepository.save(cbUser);
    return cbUser;
  }
}
