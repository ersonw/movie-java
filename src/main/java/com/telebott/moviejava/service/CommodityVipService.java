package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.entity.CommodityVip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityVipService {
    @Autowired
    private CommodityVipDao commodityVipDao;
    public void _save(CommodityVip commodityVip){
        commodityVipDao.saveAndFlush(commodityVip);
    }
}
