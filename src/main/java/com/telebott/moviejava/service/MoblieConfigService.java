package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.MoblieConfigDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.CommodityVip;
import com.telebott.moviejava.entity.MoblieConfig;
import com.telebott.moviejava.entity.OnlinePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoblieConfigService {
    @Autowired
    private MoblieConfigDao moblieConfigDao;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private OnlinePayDao onlinePayDao;
    @Autowired
    private CommodityVipDao commodityVipDao;
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
            object.put("vipBuys",_getVipBuys());
            object.put("onlinePays",_getOnlinePays());
        }
        return object;
    }
    public JSONArray _getVipBuys(){
        List<CommodityVip> commodityVipList = commodityVipDao.findAllByStatus(1);
        JSONArray array = new JSONArray();
        for (CommodityVip vip: commodityVipList) {
            JSONObject object = new JSONObject();
            object.put("id",vip.getId());
            object.put("amount",vip.getAmount());
            object.put("original",vip.getOriginal());
//            object.put("amount",Double.parseDouble(String.valueOf((vip.getAmount() / 100))));
//            object.put("original",Double.parseDouble(String.valueOf((vip.getOriginal() / 100))));
            object.put("title",vip.getTitle());
            object.put("describes",vip.getDescribes());
            object.put("image",vip.getImage());
            object.put("currency",vip.getCurrency());
            object.put("isText", vip.getIsText() == 1);
            array.add(object);
        }
        return array;
    }
    public JSONArray _getOnlinePays(){
        List<OnlinePay> onlinePayList = onlinePayDao.findAllByStatus(1);
        JSONArray array = new JSONArray();
        for (OnlinePay pay: onlinePayList) {
            JSONObject object = new JSONObject();
            object.put("title",pay.getTitle());
            object.put("iconImage",pay.getIconImage());
            object.put("id",pay.getId());
            array.add(object);
        }
        return array;
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
