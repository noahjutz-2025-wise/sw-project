package com.swdev.springbootproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Represents a TMDB movie in the database. Currently used in MovieBookmark. May be expanded to
 * include CbUser reviews.
 */
@Data
@Entity
@Table(name = "movies")
public class Movie {
  @Id private Long id;
}
