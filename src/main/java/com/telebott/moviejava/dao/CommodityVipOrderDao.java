package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityVipOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityVipOrderDao  extends JpaRepository<CommodityVipOrder, Integer>, CrudRepository<CommodityVipOrder, Integer> {
    List<CommodityVipOrder> findAllByUidAndStatus(long uid, int status);
    Page<CommodityVipOrder> findAllByUid(long uid, Pageable pageable);
    CommodityVipOrder findAllByOrderId(String id);
    CommodityVipOrder findAllByIdAndUid(long id, long uid);
}
