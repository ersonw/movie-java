package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.WaLiConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WaLiConfigDao extends JpaRepository<WaLiConfig, Long>, CrudRepository<WaLiConfig, Long> {
    WaLiConfig findAllById(long id);
    WaLiConfig findAllByName(String name);
}
