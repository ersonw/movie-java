package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoActors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoActorsDao extends JpaRepository<VideoActors, Integer>, CrudRepository<VideoActors, Integer> {
    VideoActors findAllById(long id);
}
