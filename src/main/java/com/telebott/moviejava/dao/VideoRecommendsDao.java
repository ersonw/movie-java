package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoRecommends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRecommendsDao extends JpaRepository<VideoRecommends, Integer>, CrudRepository<VideoRecommends, Integer> {
    Long countAllByVid(long vid);
    Long countAllByUid(long uid);
    Page<VideoRecommends> findAllByUidAndStatus(long uid, int status, Pageable pageable);
    VideoRecommends findAllById(long id);
    VideoRecommends findAllByUidAndVid(long uid, long vid);
    Page<VideoRecommends> findAllByVidAndStatus(long vid, int status, Pageable pageable);
    List<VideoRecommends> findAllByVidAndStatus(long vid, int status);
//    @Query(value = "SELECT *, COUNT( *)  AS c FROM `video_recommends` where add_time > :dayTime and status = 1 GROUP BY `vid` ORDER BY c DESC LIMIT 50", nativeQuery = true)
    @Query(value = "SELECT * FROM `video_recommends` where add_time > :dayTime and status = 1 GROUP BY `vid`", nativeQuery = true)
//    @Query(value = "SELECT vr.id,vr.uid,vr.vid,vr.reason,vr.status,vr.add_time,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play AS vp WHERE vp.vid=v.id))) AS c FROM `video_recommends` AS vr LEFT JOIN videos v ON v.id=vr.vid where vr.add_time>:dayTime ORDER BY c DESC", nativeQuery = true)
    Page<VideoRecommends> getAllByDateTime(long dayTime, Pageable pageable);
    @Query(value = "SELECT * FROM `video_recommends` where  status = 1 GROUP BY `vid`", nativeQuery = true)
//    @Query(value = "SELECT vr.id,vr.uid,vr.vid,vr.reason,vr.status,vr.add_time,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play AS vp WHERE vp.vid=v.id))) AS c FROM `video_recommends` AS vr LEFT JOIN videos v ON v.id=vr.vid ORDER BY c DESC", nativeQuery = true)
    Page<VideoRecommends> getAllByAll(Pageable pageable);
    @Query(value = "SELECT * FROM `video_recommends` where  status = 1 and vid= :vid order by id asc LIMIT 1", nativeQuery = true)
    VideoRecommends getFirst(long vid);
    @Query(value = "SELECT vr.id,vr.uid,vr.vid,vr.reason,vr.status,vr.add_time,((SELECT COUNT(*) FROM recommend_likes AS vl WHERE vl.rid = vr.id)+v.recommends) AS c FROM `video_recommends` AS vr  LEFT JOIN videos v ON v.id=vr.vid  WHERE vr.vid=:vid ORDER BY c DESC  LIMIT :page,:limit", nativeQuery = true)
    List<VideoRecommends> getAllComments(long vid, int page, int limit);

}
