package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CarouselBootDao;
import com.telebott.moviejava.dao.CarouselDao;
import com.telebott.moviejava.dao.SystemConfigDao;
import com.telebott.moviejava.entity.Carousel;
import com.telebott.moviejava.entity.CarouselBoot;
import com.telebott.moviejava.entity.SystemConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemConfigService {
    @Autowired
    private SystemConfigDao configDao;
    @Autowired
    private CarouselDao carouselDao;
    @Autowired
    private CarouselBootDao carouselBootDao;
//    @Autowired
//    private SystemConfigService systemConfigService;
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
    private JSONObject getCarousel(List<Carousel> configList){
        JSONArray array = new JSONArray();
        JSONObject data = new JSONObject();
        for (Carousel config: configList) {
            JSONObject object = new JSONObject();
            object.put("image", config.getImage());
            object.put("id",config.getId());
            object.put("url",config.getUrl());
            object.put("type",config.getType());
            array.add(object);
        }
        data.put("list",array);
        return data;
    }
    public JSONObject getCarousel() {
        List<Carousel> configList = carouselDao.findAllByStatus(1);
        return getCarousel(configList);
    }

    public JSONObject getBoots() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<CarouselBoot> boots = carouselBootDao.findAllByStatus(1);
        for (CarouselBoot boot : boots) {
            JSONObject json = new JSONObject();
            json.put("image",boot.getImage());
            json.put("du",boot.getDu());
            array.add(json);
        }
        object.put("list",array);
        return object;
    }

    public JSONObject getPopUpsDialog() {
        JSONObject object = new JSONObject();
        String url = getValueByKey("PopUpsDialog");
        if (url != null) {
            if (url.contains("@")){
                String[] urls = url.split("@");
                object.put("image",urls[0]);
                object.put("url",urls[1]);
            }else{
                object.put("image",url);
            }
        }
        return  object;
    }
}
