package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.WaLiConfigDao;
import com.telebott.moviejava.entity.WaLiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WaLiConfigService {
    @Autowired
    private WaLiConfigDao waLiConfigDao;
    public String getValueByName(String name) {
        WaLiConfig config = waLiConfigDao.findAllByName(name);
        if (config != null) {
            return config.getVal();
        }
        return null;
    }
}
