package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.VideoCollects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoCollectsDao extends JpaRepository<VideoCollects, Integer>, CrudRepository<VideoCollects, Integer> {
    long countAllByAid(long aid);
    Page<VideoCollects> findAllByAid(long aid, Pageable pageable);
    VideoCollects findAllById(long id);
    VideoCollects findAllByUidAndAid(long uid, long aid);
    Page<VideoCollects> findAllByUid(long uid, Pageable pageable);
}
