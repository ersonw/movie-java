package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.ShowPayOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowPayOrdersDao extends JpaRepository<ShowPayOrders, Integer>, CrudRepository<ShowPayOrders, Integer> {
    ShowPayOrders findAllById(long id);
}
