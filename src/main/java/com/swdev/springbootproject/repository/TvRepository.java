package com.swdev.springbootproject.repository;

import com.swdev.springbootproject.entity.CbTv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TvRepository extends JpaRepository<CbTv, Long> {}
