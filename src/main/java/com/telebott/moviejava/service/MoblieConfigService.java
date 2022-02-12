package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.MoblieConfigDao;
import com.telebott.moviejava.entity.MoblieConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MoblieConfigService {
    @Autowired
    private MoblieConfigDao moblieConfigDao;
    @Autowired
    private SystemConfigService systemConfigService;
    public void save(MoblieConfig config){
        moblieConfigDao.save(config);
    }
    public JSONObject getConfig(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 1, sort);
        Page<MoblieConfig> configPage = moblieConfigDao.findAll(pageable);
        JSONObject object = new JSONObject();
        if (configPage.getContent().size() > 0){
            MoblieConfig config = configPage.getContent().get(0);
            object.put("version",Double.parseDouble(config.getVersion()));
            object.put("hash",config.getHash());
            object.put("autoLogin", config.getAutoLogin() > 0);
            object.put("bootImage", config.getBootImage());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bucketName", systemConfigService.getValueByKey("bucketName"));
            jsonObject.put("endpoint", systemConfigService.getValueByKey("endpoint"));
            jsonObject.put("ossName", systemConfigService.getValueByKey("ossName"));
            object.put("ossConfig", jsonObject);
        }
        return object;
    }
    public JSONObject checkVersion(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 1, sort);
        Page<MoblieConfig> configPage = moblieConfigDao.findAll(pageable);
        JSONObject object = new JSONObject();
        if (configPage.getContent().size() > 0){
            MoblieConfig config = configPage.getContent().get(0);
            object.put("version",Double.parseDouble(config.getVersion()));
            object.put("hash",config.getHash());
            object.put("force", config.getForces() > 0);
            object.put("url", config.getUrl());
        }
        return object;
    }
}
