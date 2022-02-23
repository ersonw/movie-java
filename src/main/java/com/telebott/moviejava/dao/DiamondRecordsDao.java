package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.DiamondRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DiamondRecordsDao  extends JpaRepository<DiamondRecords, Integer>, CrudRepository<DiamondRecords, Integer> {
    DiamondRecords findAllById(long id);
    DiamondRecords findAllByIdAndUid(long id, long uid);
    Page<DiamondRecords> findAllByUid(long uid, Pageable pageable);
}
