package com.telebott.moviejava.dao;

import com.telebott.moviejava.entity.CommodityDiamond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommodityDiamondDao  extends JpaRepository<CommodityDiamond, Integer>, CrudRepository<CommodityDiamond, Integer> {
    List<CommodityDiamond> findAllByStatus(int status);
    CommodityDiamond findAllById(long id);
}
