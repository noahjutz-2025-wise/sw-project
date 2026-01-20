package com.swdev.springbootproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "posts")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String content;

  @ManyToOne
  @JoinColumn(name = "author_id")
  private CbUser author;

  @ManyToMany(cascade = CascadeType.PERSIST)
  private List<CbMovie> movies;

  @ManyToMany(cascade = CascadeType.PERSIST)
  private List<CbTv> tvs;

  public Post(String content) {
    this.content = content;
  }
}
