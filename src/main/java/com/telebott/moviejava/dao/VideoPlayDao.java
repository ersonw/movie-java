package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoPlay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoPlayDao extends JpaRepository<VideoPlay, Integer>, CrudRepository<VideoPlay, Integer> {
    Long countAllByVid(long vid);
    Long countAllByUid(long uid);
}
