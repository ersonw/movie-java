package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoFeaturedRecords;
import com.telebott.moviejava.entity.VideoFeatureds;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoFeaturedRecordsDao extends JpaRepository<VideoFeaturedRecords, Integer>, CrudRepository<VideoFeaturedRecords, Integer> {
    List<VideoFeaturedRecords> findAllByFid(long fid);
    Page<VideoFeaturedRecords> findAllByFid(long fid, Pageable pageable);
//    @Query(value = "SELECT *, COUNT( *)  AS c FROM `search_tags` GROUP BY `context` ORDER BY c DESC LIMIT 50", nativeQuery = true)
}
