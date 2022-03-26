package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.WithdrawalRecords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WithdrawalRecordsDao extends JpaRepository<WithdrawalRecords, Integer>, CrudRepository<WithdrawalRecords, Integer> {
    WithdrawalRecords findAllById(long id);
    Page<WithdrawalRecords> findAllByUid(long uid, Pageable pageable);
}
