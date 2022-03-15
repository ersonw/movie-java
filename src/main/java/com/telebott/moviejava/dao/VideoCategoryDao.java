package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface VideoCategoryDao extends JpaRepository<VideoCategory, Integer>, CrudRepository<VideoCategory, Integer> {
    VideoCategory findAllById(long id);
    List<VideoCategory> findAllByStatus(int status);
}
