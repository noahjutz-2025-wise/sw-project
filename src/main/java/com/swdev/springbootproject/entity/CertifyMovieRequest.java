package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "certify_movie_request")
public class CertifyMovieRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(nullable = false)
  private Long movieId;

  @ManyToOne
  @JoinColumn(name = "critic_id", nullable = false)
  private CbUser cbUser;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private RequestStatus status = RequestStatus.PENDING;

  public enum RequestStatus {
    PENDING,
    APPROVED,
    REJECTED
  }
}
