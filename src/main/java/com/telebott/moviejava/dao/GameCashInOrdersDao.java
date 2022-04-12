package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.GameCashInOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameCashInOrdersDao  extends JpaRepository<GameCashInOrders, Long>, CrudRepository<GameCashInOrders, Long> {
    GameCashInOrders findAllById(long id);
    Page<GameCashInOrders> findAllByUid(long uid, Pageable pageable);
    GameCashInOrders findAllByOrderId(String orderId);

}
