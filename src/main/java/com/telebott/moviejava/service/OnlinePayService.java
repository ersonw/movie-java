package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.GameCashInDao;
import com.telebott.moviejava.dao.GameCashInOrdersDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public JSONObject getCashInOrders(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        int page = 0;
        if (data != null && data.get("page") != null && UserService.isNumberString(data.getString("page"))) page = data.getInteger("page");
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        page--;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<GameCashInOrders> orders = gameCashInOrdersDao.findAllByUid(user.getId(), pageable);
        for (GameCashInOrders order: orders.getContent()) {
            JSONObject json = new JSONObject();
            json.put("id",order.getId());
            json.put("orderId", order.getOrderId());
            json.put("amount",order.getAmount());
            json.put("updateTime",order.getUpdateTime());
            json.put("status",order.getStatus());
            array.add(json);
        }
        object.put("total", orders.getTotalPages());
        object.put("list",array);
        return object;
    }
}
