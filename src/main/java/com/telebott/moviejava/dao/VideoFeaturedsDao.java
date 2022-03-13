package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoFeatureds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoFeaturedsDao extends JpaRepository<VideoFeatureds, Integer>, CrudRepository<VideoFeatureds, Integer> {
    List<VideoFeatureds> findAllByStatus(int status);
    VideoFeatureds findAllById(long id);
}
