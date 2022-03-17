package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
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
    @Autowired
    private CommodityDiamondDao commodityDiamondDao;
    @Autowired
    private CommodityGoldDao commodityGoldDao;
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
            object.put("domain", systemConfigService.getValueByKey("domain"));
            object.put("groupLink", systemConfigService.getValueByKey("groupLink"));
            object.put("ossConfig", jsonObject);
            object.put("vipBuys",_getVipBuys());
            object.put("onlinePays",_getOnlinePays());
            object.put("buyDiamonds",_getBuyDiamonds());
            object.put("buyGolds",_getBuyGolds());
        }
        return object;
    }
 private JSONArray _getBuyGolds(){
     JSONArray array = new JSONArray();
     List<CommodityGold> commodityGolds = commodityGoldDao.findAllByStatus(1);
     for (CommodityGold diamond: commodityGolds) {
         JSONObject object = new JSONObject();
         object.put("id", diamond.getId());
         object.put("amount",diamond.getAmount());
         object.put("gold", diamond.getGold());
         array.add(object);
     }
     return array;
 }
    private JSONArray _getBuyDiamonds() {
        JSONArray array = new JSONArray();
        List<CommodityDiamond> commodityDiamonds = commodityDiamondDao.findAllByStatus(1);
        for (CommodityDiamond diamond: commodityDiamonds) {
            JSONObject object = new JSONObject();
            object.put("id", diamond.getId());
            object.put("amount",diamond.getAmount());
            object.put("diamond", diamond.getDiamond());
            array.add(object);
        }
        return array;
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
            object.put("urlIos", systemConfigService.getValueByKey("urlIos"));
            object.put("urlAndroid", systemConfigService.getValueByKey("urlAndroid"));
        }
        return object;
    }
}
