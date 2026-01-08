package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbMovie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<CbMovie, Long> {}
