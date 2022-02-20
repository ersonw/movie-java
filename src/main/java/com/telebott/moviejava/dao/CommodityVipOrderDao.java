package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityVipOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityVipOrderDao  extends JpaRepository<CommodityVipOrder, Integer>, CrudRepository<CommodityVipOrder, Integer> {
}
