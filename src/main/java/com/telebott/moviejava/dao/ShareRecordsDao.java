package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.ShareRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShareRecordsDao extends JpaRepository<ShareRecords, Integer>, CrudRepository<ShareRecords, Integer> {
    ShareRecords findAllById(long id);
    ShareRecords findAllByUidAndToUid(long uid, long tid);
    ShareRecords findAllByToUid(long tid);
    Page<ShareRecords> findAllByUid(long uid, Pageable pageable);
    Page<ShareRecords> findAllByToUid(long tid, Pageable pageable);
    long countAllByUid(long uid);
}
