package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityDiamondOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommodityDiamondOrderDao   extends JpaRepository<CommodityDiamondOrder, Integer>, CrudRepository<CommodityDiamondOrder, Integer> {
    List<CommodityDiamondOrder> findAllByUidAndStatus(long uid, int status);
    CommodityDiamondOrder findAllByOrderId(String orderId);
    Page<CommodityDiamondOrder> findAllByUid(long uid, Pageable pageable);
    CommodityDiamondOrder findAllByIdAndUid(long id, long uid);
}
