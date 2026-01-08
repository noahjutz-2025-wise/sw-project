package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ratings")
public class UserMovieRating {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "cb_user", referencedColumnName = "username", nullable = false)
  private CbUser cbUser;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "movie_id", referencedColumnName = "id", nullable = false)
  private CbMovie movie;

  @Min(value = 0, message = "Die Note muss eine Ganzzahl zwischen 0 und 10 sein.")
  @Max(value = 10, message = "Die Note muss eine Ganzzahl zwischen 0 und 10 sein.")
  @Column(nullable = false)
  private Integer rating;

  @Column(nullable = false)
  private LocalDateTime ratedAt = LocalDateTime.now();

  public UserMovieRating(CbUser cbUser, CbMovie movie, Integer rating) {
    this.cbUser = cbUser;
    this.movie = movie;
    this.rating = rating;
  }
}
