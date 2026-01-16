package com.swdev.springbootproject.service;

import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertifiedBangerService {
  private final CertifiedBangerRepository certifiedBangerRepository;

  public void applyCertifiedBangerFlag(List<MediaDto> movies) {
    Set<Long> ids = movies.stream().map(MediaDto::getId).collect(Collectors.toSet());

    Set<Long> certifiedBangersIds =
        certifiedBangerRepository.findAllById(ids).stream()
            .map(CertifiedBanger::getId)
            .collect(Collectors.toSet());

    movies.forEach(movie -> movie.setCertifiedBanger(certifiedBangersIds.contains(movie.getId())));
  }
}
