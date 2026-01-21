package com.swdev.springbootproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

import java.util.List;

/**
 * Represents a TMDB movie in the database. Currently used in MovieBookmark. May be expanded to
 * include CbUser reviews.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "movies")
public class CbMovie {
  @NonNull @Id private Long id;
  @ManyToMany(mappedBy = "movies")
  private List<Post> posts;
}
