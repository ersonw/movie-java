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
    Page<VideoActors> findAllByNameLikeAndStatus(String name, int status, Pageable pageable);
    Page<VideoActors> findAllByMeasurements(long mid, Pageable pageable);
    long countAllByMeasurements(long mid);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM video_collects as vc WHERE vc.aid=va.id)AS c FROM video_actors AS va ORDER BY c DESC LIMIT  :page,:limit", nativeQuery = true)
    List<VideoActors> getHots(int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM video_collects as vc WHERE vc.aid=va.id)AS c FROM video_actors AS va where va.measurements=:mid ORDER BY c DESC LIMIT  :page,:limit", nativeQuery = true)
    List<VideoActors> getHotsByTag(long mid,int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM videos as v WHERE va.id=v.actor) AS c FROM video_actors AS va WHERE 1 ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<VideoActors> getAllByWorks(int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(SELECT COUNT(*) FROM videos as v WHERE va.id=v.actor) AS c FROM video_actors AS va WHERE va.measurements=:mid ORDER BY c DESC  LIMIT :page,:limit", nativeQuery = true)
    List<VideoActors> getAllByWorks(long mid,int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(IF(v.play>0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id))) AS c FROM `video_actors` AS va LEFT JOIN videos v ON v.actor=va.id WHERE 1  GROUP BY va.id  ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<VideoActors> getAllByPlays(int page, int limit);
    @Query(value = "SELECT va.id,va.name,va.avatar,va.measurements,va.status,va.add_time,va.update_time,(IF(v.play>0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id))) AS c FROM `video_actors` AS va LEFT JOIN videos v ON v.actor=va.id WHERE va.measurements=:mid  GROUP BY va.id  ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<VideoActors> getAllByPlays(long mid,int page, int limit);
}
