package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoRecommends;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRecommendsDao extends JpaRepository<VideoRecommends, Integer>, CrudRepository<VideoRecommends, Integer> {
}
