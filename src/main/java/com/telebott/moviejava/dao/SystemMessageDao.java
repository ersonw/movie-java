package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.SystemMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemMessageDao extends JpaRepository<SystemMessage, Integer>, CrudRepository<SystemMessage, Integer> {
}
