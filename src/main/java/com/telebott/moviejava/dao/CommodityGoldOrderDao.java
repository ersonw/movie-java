package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityDiamondOrder;
import com.telebott.moviejava.entity.CommodityGoldOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityGoldOrderDao   extends JpaRepository<CommodityGoldOrder, Integer>, CrudRepository<CommodityGoldOrder, Integer> {
    CommodityGoldOrder findAllByOrderId(String orderId);
    List<CommodityGoldOrder> findAllByUidAndStatus(long uid, int status);
    CommodityGoldOrder findAllByIdAndUid(long id, long uid);
    Page<CommodityGoldOrder> findAllByUid(long uid, Pageable pageable);
}
