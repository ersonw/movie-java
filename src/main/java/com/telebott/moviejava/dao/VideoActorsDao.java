package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoActors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoActorsDao extends JpaRepository<VideoActors, Integer>, CrudRepository<VideoActors, Integer> {
    VideoActors findAllById(long id);
    Page<VideoActors> findAllByMeasurements(long mid, Pageable pageable);

    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM video_collects as vc WHERE vc.aid=va.id)AS c FROM video_actors AS va ORDER BY c DESC LIMIT  :page,:limit", nativeQuery = true)
    List<VideoActors> getHots(int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM video_collects as vc WHERE vc.aid=va.id)AS c FROM video_actors AS va where va.measurements=:mid ORDER BY c DESC LIMIT  :page,:limit", nativeQuery = true)
    List<VideoActors> getHotsByTag(long mid,int page, int limit);
}
