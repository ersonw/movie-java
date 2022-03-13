package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.Videos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
}
