package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.OnlinePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlinePayService {
    @Autowired
    private OnlinePayDao onlinePayDao;
    public void _save(OnlinePay onlinePay){
        onlinePayDao.saveAndFlush(onlinePay);
    }
}
