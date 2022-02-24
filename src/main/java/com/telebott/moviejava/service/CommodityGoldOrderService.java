package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityGoldDao;
import com.telebott.moviejava.dao.CommodityGoldOrderDao;
import com.telebott.moviejava.dao.GoldRecordsDao;
import com.telebott.moviejava.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityGoldOrderService {
    @Autowired
    private CommodityGoldOrderDao commodityGoldOrderDao;
    @Autowired
    private CommodityGoldDao commodityGoldDao;
    @Autowired
    private GoldRecordsDao goldRecordsDao;

    public JSONObject _crateOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        List<CommodityGoldOrder> orderList = commodityGoldOrderDao.findAllByUidAndStatus(user.getId(),0);
        CommodityGold commodityGold = commodityGoldDao.findAllById(Long.parseLong(id));
        if (orderList.size() > 0){
            object.put("crate", false);
            object.put("id",orderList.get(0).getOrderId());
        }else if (commodityGold != null){
            long time = System.currentTimeMillis();
            CommodityGoldOrder order = new CommodityGoldOrder();
            order.setUid(user.getId());
            order.setCid(commodityGold.getId());
            order.setAmount(commodityGold.getAmount());
            order.setCtime(time);
            order.setStatus(0);
            order.setOrderId(time +String.valueOf(user.getId()));
            commodityGoldOrderDao.saveAndFlush(order);
            object.put("crate", true);
            object.put("id",order.getOrderId());
        }
        return object;
    }
    public JSONObject _cancelOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        CommodityGoldOrder order = commodityGoldOrderDao.findAllByIdAndUid(Long.parseLong(id),user.getId());
        if (order != null){
            order.setStatus(-1);
            commodityGoldOrderDao.saveAndFlush(order);
        }
        object.put("state","ok");
        return object;
    }
    public JSONObject _getOrder(Users user, String data) {
        JSONObject objectData = JSONObject.parseObject(data);
        int page = 0;
        if (objectData.get("page") != null){
            page = (Integer.parseInt(objectData.get("page").toString()) - 1);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<CommodityGoldOrder> orders = commodityGoldOrderDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list",_getList(orders.getContent()));
        object.put("total",orders.getTotalPages());
        return object;
    }
    private JSONArray _getList(List<CommodityGoldOrder> content) {
        JSONArray array = new JSONArray();
        for (CommodityGoldOrder order: content) {
            JSONObject object = new JSONObject();
            object.put("id",order.getId());
            object.put("ctime",order.getCtime());
            object.put("orderId",order.getOrderId());
            object.put("status", order.getStatus());
            object.put("amount", order.getAmount());
            array.add(object);
        }
        return array;
    }
    public JSONObject _getRecords(Users user, String data) {
        JSONObject objectData = JSONObject.parseObject(data);
        int page = 0;
        if (objectData.get("page") != null){
            page = (Integer.parseInt(objectData.get("page").toString()) - 1);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<GoldRecords> diamondRecords = goldRecordsDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list", diamondRecords.getContent());
        object.put("total", diamondRecords.getTotalPages());
        return object;
    }
}
