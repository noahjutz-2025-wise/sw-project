package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(MovieBookmarkId.class)
@Table(
    name = "movie_bookmarks",
    uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "movie_id"})})
public class MovieBookmark {
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private CbUser user;

  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  private CbMovie movie;

  @Enumerated(EnumType.STRING)
  private BookmarkStatus status;
}

class MovieBookmarkId {
  private Long user;
  private Long movie;
}
