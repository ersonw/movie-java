package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.GameCashIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCashInDao  extends JpaRepository<GameCashIn, Long>, CrudRepository<GameCashIn, Long> {
    GameCashIn findAllById(long id);
}
