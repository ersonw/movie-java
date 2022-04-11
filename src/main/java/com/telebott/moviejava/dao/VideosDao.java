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
//    Page<Videos> findAllByUidAndStatus(long uid, int status, Pageable pageable);
    Page<Videos> findAllByStatus(int status,Pageable pageable);
    Page<Videos> findAllByStatusAndDiamond(int status, long diamond,Pageable pageable);
    Page<Videos> findAllByVodClassAndStatus(long classId,int status,Pageable pageable);
    Page<Videos> findAllByVodClassAndStatusAndDiamond(long classId,int status, long diamond,Pageable pageable);
    Page<Videos> findAllByTitleLikeAndStatus(String likes,int status,Pageable pageable);
    Page<Videos> findAllByVodClassAndTitleLikeAndStatus(long classId,String likes,int status,Pageable pageable);
    long countAllByVodClassAndTitleLikeAndStatus(long classId,String likes, int status);
    long countAllByTitleLikeAndStatus(String likes, int status);
    long countAllByVodClassAndStatus(long classId, int status);
    long countAllByStatusAndDiamond(int status, long diamond);
    long countAllByVodClassAndStatusAndDiamond(long classId, int status, long diamond);
    long countAllByStatus(int status);
    Videos findAllByShareId(String id);
    long countAllByActor(long actor);
    long countAllByUidAndStatus(long uid, int status);
    Page<Videos> findAllByUidAndStatus(long uid, int status, Pageable pageable);
    @Query(value = "select * from videos where status =1 and (title like %:sTitle% or title like %:tTitle%) ", nativeQuery = true)
    Page<Videos> findByAv(String sTitle, String tTitle, Pageable pageable);
    @Query(value = "select * from videos where status =1 and (title like %:sTitle% or title like %:tTitle%) ", nativeQuery = true)
    Page<Videos> findByWork(String sTitle, String tTitle,Pageable pageable);
    @Query(value = "select * from videos where status =1 and numbers like %:numbers% ", nativeQuery = true)
    Page<Videos> findByNumber(String numbers,Pageable pageable);
    @Query(value = "SELECT v.id,v.uid,v.title,v.numbers,v.pic_thumb,v.gif_thumb,v.vod_time_add,v.vod_time_update,v.vod_class,v.vod_duration,v.vod_play_url,v.vod_content,v.vod_down_url,v.share_id,v.vod_tag,v.actor,v.diamond,v.status,v.play,v.recommends,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id)))AS c FROM  videos AS v  ORDER BY c DESC LIMIT 12", nativeQuery = true)
    List<Videos> findAllHots();
    @Query(value = "SELECT v.id,v.uid,v.title,v.numbers,v.pic_thumb,v.gif_thumb,v.vod_time_add,v.vod_time_update,v.vod_class,v.vod_duration,v.vod_play_url,v.vod_content,v.vod_down_url,v.share_id,v.vod_tag,v.actor,v.diamond,v.status,v.play,v.recommends,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id AND vp.add_time > :time)))AS c FROM  videos AS v  ORDER BY c DESC LIMIT 24 ", nativeQuery = true)
    List<Videos> findAllHots(long time);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(int page, int limit);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 and diamond=:diamond ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(int page, int limit, long diamond);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 and vod_class=:cid ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(long cid,int page, int limit);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 and vod_class=:cid and diamond=:diamond ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(long cid,int page, int limit, long diamond);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 and vod_class=:cid AND title LIKE :likes ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(long cid, String likes,int page, int limit);
    @Query(value = "SELECT *,(IF(play > 0,play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=id)))AS c FROM videos WHERE status=1 and title LIKE :likes ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getAllByClass(String likes,int page, int limit);
    @Query(value = "SELECT v.id,v.uid,v.title,v.numbers,v.pic_thumb,v.gif_thumb,v.vod_time_add,v.vod_time_update,v.vod_class,v.vod_duration,v.vod_play_url,v.vod_content,v.vod_down_url,v.share_id,v.vod_tag,v.actor,v.diamond,v.status,v.play,v.recommends,(IF(v.play > 0,v.play,(SELECT COUNT(*) FROM video_play vp WHERE vp.vid=v.id)))AS c FROM video_actors AS va LEFT JOIN videos v ON v.actor=va.id and v.status=1 WHERE va.id=:aid ORDER BY c DESC LIMIT :page,:limit", nativeQuery = true)
    List<Videos> getPlay(long aid, int page, int limit);
    Page<Videos> findAllByActorAndStatus(long aid, int status,Pageable pageable);


}
