package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigDao extends JpaRepository<SystemConfig, Integer>, CrudRepository<SystemConfig, Integer> {
    SystemConfig findAllById(int id);
    SystemConfig findAllByName(String name);
    List<SystemConfig> findAllByNameAndDes(String name,String des);
}
