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
@Table(name = "posts_to_tv")
@IdClass(PostToCbTv.PostToCbTvId.class)
public class PostToCbTv {

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private Post post;

  @Id
  @ManyToOne
  @JoinColumn(name = "tv_id", nullable = false)
  private CbTv tv;

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PostToCbTvId implements Serializable {
    private Long post;
    private Long tv;
  }
}
