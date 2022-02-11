package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.SystemConfigDao;
import com.telebott.moviejava.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemConfigService {
    @Autowired
    private SystemConfigDao configDao;
    public void _save(SystemConfig config){
        configDao.saveAndFlush(config);
    }
    public String getValueByKey(String key){
        SystemConfig config = configDao.findAllByName(key);
        if (config != null){
            return config.getVal();
        }
        return null;
    }
    public SystemConfig getById(int id){
        return configDao.findAllById(id);
    }
}
