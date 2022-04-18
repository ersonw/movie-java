package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.MoblieConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MoblieConfigDao extends JpaRepository<MoblieConfig, Long>, CrudRepository<MoblieConfig, Long> {
    MoblieConfig findAllById(int id);
    Page<MoblieConfig> findAll(Pageable pageable);
}
