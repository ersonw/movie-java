package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.ExpiredRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpiredRecordsDao extends JpaRepository<ExpiredRecords, Integer>, CrudRepository<ExpiredRecords, Integer> {
    ExpiredRecords findAllByUid(long uid);
}
