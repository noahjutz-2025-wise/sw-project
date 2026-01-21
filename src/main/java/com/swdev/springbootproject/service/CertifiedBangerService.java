package com.swdev.springbootproject.service;

import com.swdev.springbootproject.entity.CertifiedBanger;
import com.swdev.springbootproject.entity.CertifyMovieRequest;
import com.swdev.springbootproject.model.dto.MediaDto;
import com.swdev.springbootproject.repository.CertifiedBangerRepository;
import com.swdev.springbootproject.repository.CertifyMovieRequestRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertifiedBangerService {
  private final CertifiedBangerRepository certifiedBangerRepository;
  private final CertifyMovieRequestRepository certifyMovieRequestRepository;

  public void applyCertifiedBangerFlag(List<MediaDto> movies) {
    Set<Long> ids = movies.stream().map(MediaDto::getId).collect(Collectors.toSet());

    Set<Long> certifiedBangersIds =
        certifiedBangerRepository.findAllById(ids).stream()
            .map(CertifiedBanger::getId)
            .collect(Collectors.toSet());

    Set<Long> pendingRequestIds =
        certifyMovieRequestRepository
            .findByStatusOrderByIdDesc(CertifyMovieRequest.RequestStatus.PENDING)
            .stream()
            .map(CertifyMovieRequest::getMovieId)
            .collect(Collectors.toSet());

    movies.forEach(
        movie ->
            movie.setCertifiedBangerStatus(
                certifiedBangersIds.contains(movie.getId())
                    ? MediaDto.CertifiedBangerStatus.CERTIFIED
                    : pendingRequestIds.contains(movie.getId())
                        ? MediaDto.CertifiedBangerStatus.REQUEST_PENDING
                        : MediaDto.CertifiedBangerStatus.NOT_CERTIFIED));
  }
}
