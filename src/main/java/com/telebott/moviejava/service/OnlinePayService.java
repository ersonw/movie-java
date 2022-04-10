package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.OnlinePay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlinePayService {
    @Autowired
    private OnlinePayDao onlinePayDao;
    public void _save(OnlinePay onlinePay){
        onlinePayDao.saveAndFlush(onlinePay);
    }

    public JSONObject getOnlinePays() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<OnlinePay> onlinePayList = onlinePayDao.findAllByStatus(1);
        for(OnlinePay onlinePay : onlinePayList){
            JSONObject json = new JSONObject();
            json.put("id",onlinePay.getId());
            json.put("title",onlinePay.getTitle());
            json.put("type",onlinePay.getType());
            json.put("iconImage",onlinePay.getIconImage());
            array.add(json);
        }
        object.put("list", array);
        return object;
    }
}
