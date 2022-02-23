package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityDiamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommodityDiamondDao  extends JpaRepository<CommodityDiamond, Integer>, CrudRepository<CommodityDiamond, Integer> {
    List<CommodityDiamond> findAllByStatus(int status);
    CommodityDiamond findAllById(long id);
}
