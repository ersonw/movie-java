package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Marquee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarqueeDao extends JpaRepository<Marquee, Long>, CrudRepository<Marquee, Long> {
    Marquee findAllById(long id);
}
