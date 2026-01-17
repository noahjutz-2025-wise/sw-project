package com.swdev.springbootproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tvs")
public class CbTv {
  @NonNull @Id private Long id;
}
