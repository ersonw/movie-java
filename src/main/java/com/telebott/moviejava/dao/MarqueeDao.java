package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Marquee;
import com.telebott.moviejava.entity.MoblieConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarqueeDao extends JpaRepository<MoblieConfig, Long>, CrudRepository<MoblieConfig, Long> {
    Marquee findAllById(long id);
}
