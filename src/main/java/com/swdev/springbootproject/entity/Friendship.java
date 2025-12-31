package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "friendship")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Friendship {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cbuser1_id", nullable = false)
  private CbUser cbUser1;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cbuser2_id", nullable = false)
  private CbUser cbUser2;

  @Column(nullable = false)
  @Builder.Default
  private LocalDateTime createdAt = LocalDateTime.now();
}
