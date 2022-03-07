package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCommentsDao extends JpaRepository<VideoComments, Integer>, CrudRepository<VideoComments, Integer> {
}
