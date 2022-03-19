package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.ShowPay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowPayDao  extends JpaRepository<ShowPay, Integer>, CrudRepository<ShowPay, Integer> {
    ShowPay findAllById(long id);
}
