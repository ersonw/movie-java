package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.CommodityVipOrderDao;
import com.telebott.moviejava.entity.CommodityVipOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityVipOrderService {
    @Autowired
    private CommodityVipOrderDao commodityVipOrderDao;
    public void _save(CommodityVipOrder commodityVipOrder){
        commodityVipOrderDao.saveAndFlush(commodityVipOrder);
    }
}
