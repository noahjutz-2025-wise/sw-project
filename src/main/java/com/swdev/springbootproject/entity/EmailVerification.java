package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "email_verification")
public class EmailVerification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "verification_token", unique = true)
  private String verificationToken;

  @Column(name = "token_expiry_date")
  private LocalDateTime tokenExpiryDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private CbUser cbUser;

  public EmailVerification(String verificationToken, LocalDateTime tokenExpiryDate, CbUser cbUser) {
    this.verificationToken = verificationToken;
    this.tokenExpiryDate = tokenExpiryDate;
    this.cbUser = cbUser;
  }
}
