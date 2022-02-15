package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.SmsRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface SmsRecordsDao extends JpaRepository<SmsRecords, Integer>, CrudRepository<SmsRecords, Integer> {
    SmsRecords findAllByData(String data);
    SmsRecords findAllById(int id);
}
