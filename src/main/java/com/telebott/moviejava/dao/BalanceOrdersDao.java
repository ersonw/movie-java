package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.BalanceOrders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceOrdersDao extends JpaRepository<BalanceOrders, Integer>, CrudRepository<BalanceOrders, Integer> {
    BalanceOrders findAllById(long id);
    Page<BalanceOrders> findAllByUidAndStatus(long uid, int status, Pageable pageable);
    Page<BalanceOrders> findAllByUid(long uid,Pageable pageable);
    long countAllByUidAndStatus(long uid, int status);
    long countAllByUid(long uid);
    @Query(value = "SELECT SUM(amount) FROM balance_orders WHERE uid=:uid AND status=1", nativeQuery = true)
    long countAllByBalance(long uid);
}
