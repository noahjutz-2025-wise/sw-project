package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CertifyMovieRequest;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertifyMovieRequestRepository
    extends JpaRepository<@NonNull CertifyMovieRequest, @NonNull Long> {
  List<CertifyMovieRequest> findByStatusOrderByIdDesc(CertifyMovieRequest.RequestStatus status);

  boolean existsByMovieIdAndCbUser_IdAndStatus(
      Long movieId, Long criticId, CertifyMovieRequest.RequestStatus status);
}
