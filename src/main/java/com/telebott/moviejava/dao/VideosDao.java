package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideosDao extends JpaRepository<Videos, Integer>, CrudRepository<Videos, Integer> {
    Videos findAllById(long id);
    Videos findAllByIdAndStatus(long id, int status);
    Page<Videos> findAllByStatus(int status,Pageable pageable);
    Videos findAllByShareId(String id);
    long countAllByActor(long actor);
    long countAllByUid(long uid);
    @Query(value = "select * from videos where status =1 and (title like %:sTitle% or title like %:tTitle%) ", nativeQuery = true)
    Page<Videos> findByAv(String sTitle, String tTitle, Pageable pageable);
    @Query(value = "select * from videos where status =1 and (title like %:sTitle% or title like %:tTitle%) ", nativeQuery = true)
    Page<Videos> findByWork(Pageable pageable);
    @Query(value = "select * from videos where status =1 and numbers like %:numbers% ", nativeQuery = true)
    Page<Videos> findByNumber(String numbers,Pageable pageable);
    @Query(value = "SELECT v.id,v.uid,v.title,v.numbers,v.pic_thumb,v.gif_thumb,v.vod_time_add,v.vod_time_update,v.vod_class,v.vod_duration,v.vod_play_url,v.vod_content,v.vod_down_url,v.share_id,v.vod_tag,v.actor,v.diamond,v.status,v.play,v.recommends,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id)))AS c FROM video_actors AS va LEFT JOIN videos v ON v.actor=va.id and v.status=1 WHERE va.id=:aid ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getPlay(long aid, int page, int limit);
    Page<Videos> findAllByActorAndStatus(long aid, int status,Pageable pageable);

}
