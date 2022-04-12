package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.WaliGameRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaliGameRecordsDao  extends JpaRepository<WaliGameRecords, Long>, CrudRepository<WaliGameRecords, Long> {
    WaliGameRecords findAllById(long id);
    List<WaliGameRecords> findAllByUid(long uid);
    @Query(value = "SELECT id,game_id,uid,add_time,COUNT(*) AS c FROM wali_game_records WHERE uid=:uid GROUP BY game_id ORDER BY c DESC", nativeQuery = true)
    List<WaliGameRecords> getRecords(long uid);
}
