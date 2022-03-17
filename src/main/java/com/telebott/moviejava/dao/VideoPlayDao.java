package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoPlay;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoPlayDao extends JpaRepository<VideoPlay, Integer>, CrudRepository<VideoPlay, Integer> {
    Long countAllByVid(long vid);
    Long countAllByUid(long uid);
    @Query(value = "SELECT * FROM video_play  WHERE uid=:uid GROUP BY vid ORDER BY id DESC", nativeQuery = true)
    Page<VideoPlay> findRecords(long uid, Pageable pageable);
}
