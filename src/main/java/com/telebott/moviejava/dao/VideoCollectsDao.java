package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoCollects;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCollectsDao extends JpaRepository<VideoCollects, Integer>, CrudRepository<VideoCollects, Integer> {
}
