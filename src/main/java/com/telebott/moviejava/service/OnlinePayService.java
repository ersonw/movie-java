package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.GameCashInDao;
import com.telebott.moviejava.dao.GameCashInOrdersDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlinePayService {
    @Autowired
    private OnlinePayDao onlinePayDao;
    @Autowired
    private GameCashInDao gameCashInDao;
    @Autowired
    private GameCashInOrdersDao gameCashInOrdersDao;
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

    public JSONObject getCashIns() {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<GameCashIn> cashIns = gameCashInDao.findAll();
        for (GameCashIn cashIn: cashIns) {
            JSONObject json = new JSONObject();
            json.put("id",cashIn.getId());
            json.put("vip", cashIn.getVip());
            json.put("amount",cashIn.getAmount());
            array.add(json);
        }
        object.put("list",array);
        return object;
    }

    public JSONObject _crateOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        object.put("crate", false);
        GameCashIn cashIn = gameCashInDao.findAllById(Long.parseLong(id));
        if (cashIn != null){
            GameCashInOrders order = new GameCashInOrders();
            order.setStatus(0);
            order.setCid(cashIn.getId());
            order.setOrderId(TimeUtil._getOrderNo());
            order.setAddTime(System.currentTimeMillis());
            order.setUpdateTime(System.currentTimeMillis());
            order.setUid(user.getId());
            order.setAmount(cashIn.getAmount());
            gameCashInOrdersDao.saveAndFlush(order);
            object.put("crate", true);
            object.put("id",order.getOrderId());
        }else{
            object.put("msg", "金额已过期，请刷新重试!");
        }
        return object;
    }
}
