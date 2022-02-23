package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityDiamondDao;
import com.telebott.moviejava.dao.CommodityDiamondOrderDao;
import com.telebott.moviejava.dao.DiamondRecordsDao;
import com.telebott.moviejava.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityDiamondOrderService {
    @Autowired
    private CommodityDiamondOrderDao commodityDiamondOrderDao;
    @Autowired
    private CommodityDiamondDao commodityDiamondDao;
    @Autowired
    private DiamondRecordsDao diamondRecordsDao;

    public JSONObject _crateOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        List<CommodityDiamondOrder> orderList = commodityDiamondOrderDao.findAllByUidAndStatus(user.getId(),0);
        CommodityDiamond commodityDiamond = commodityDiamondDao.findAllById(Long.parseLong(id));
        if (orderList.size() > 0){
            object.put("crate", false);
            object.put("id",orderList.get(0).getOrderId());
        }else if (commodityDiamond != null){
            long time = System.currentTimeMillis();
            CommodityDiamondOrder order = new CommodityDiamondOrder();
            order.setUid(user.getId());
            order.setCid(commodityDiamond.getId());
            order.setAmount(commodityDiamond.getAmount());
            order.setCtime(time);
            order.setStatus(0);
            order.setOrderId(time +String.valueOf(user.getId()));
            commodityDiamondOrderDao.saveAndFlush(order);
            object.put("crate", true);
            object.put("id",order.getOrderId());
        }
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
        Page<CommodityDiamondOrder> diamondOrders = commodityDiamondOrderDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list",_getList(diamondOrders.getContent()));
        object.put("total",diamondOrders.getTotalPages());
        return object;
    }
    private JSONArray _getList(List<CommodityDiamondOrder> content) {
        JSONArray array = new JSONArray();
        for (CommodityDiamondOrder order: content) {
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

    public JSONObject _cancelOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        CommodityDiamondOrder order = commodityDiamondOrderDao.findAllByIdAndUid(Long.parseLong(id),user.getId());
        if (order != null){
            order.setStatus(-1);
            commodityDiamondOrderDao.saveAndFlush(order);
        }
        object.put("state","ok");
        return object;
    }

    public JSONObject _getRecords(Users user, String data) {
        JSONObject objectData = JSONObject.parseObject(data);
        int page = 0;
        if (objectData.get("page") != null){
            page = (Integer.parseInt(objectData.get("page").toString()) - 1);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<DiamondRecords> diamondRecords = diamondRecordsDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list", diamondRecords.getContent());
        object.put("total", diamondRecords.getTotalPages());
        return object;
    }
}
