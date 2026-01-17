package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts_to_movies")
@IdClass(PostToCbMovie.PostToCbMovieId.class)
public class PostToCbMovie {

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Id
  @ManyToOne
  @JoinColumn(name = "movie_id", nullable = false)
  private CbMovie movie;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PostToCbMovieId implements Serializable {
    private Long post;
    private Long movie;
  }
}
