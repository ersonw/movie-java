package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.CommodityVipOrderDao;
import com.telebott.moviejava.dao.OnlineOrderDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OnlineOrderService {
    private static final int PAY_ONLINE_VIP = 100;
    private static final int PAY_ONLINE_GOLD = 101;
    private static final int PAY_ONLINE_DIAMOND = 102;
    @Autowired
    private OnlineOrderDao onlineOrderDao;
    @Autowired
    private OnlinePayDao onlinePayDao;
    @Autowired
    private CommodityVipDao commodityVipDao;
    @Autowired
    private CommodityVipOrderDao commodityVipOrderDao;
    @Autowired
    private CommodityVipOrderService commodityVipOrderService;
    @Autowired
    private UserService userService;
    public void _save(OnlineOrder onlineOrder){
        onlineOrderDao.saveAndFlush(onlineOrder);
    }

    public JSONObject getResult(CommodityVip commodityVip){
        JSONObject object = new JSONObject();
        object.put("title",commodityVip.getTitle());
        object.put("currency",commodityVip.getCurrency());
        object.put("describes",commodityVip.getDescribes());
        return object;
    }
    public JSONObject _getOrder(String type, String order_id) {
        JSONObject object = new JSONObject();
        switch (Integer.parseInt(type)){
            case PAY_ONLINE_VIP:
                CommodityVipOrder commodityVipOrder = commodityVipOrderDao.findAllByOrderId(order_id);
                if (commodityVipOrder != null){
                    CommodityVip commodityVip = commodityVipDao.findAllById(commodityVipOrder.getCid());
                    object = getResult(commodityVip);
                    object.put("amount",commodityVipOrder.getAmount());
                }
                break;
            case PAY_ONLINE_GOLD:
                break;
            case PAY_ONLINE_DIAMOND:
                break;
            default:
                break;
        }
        return object;
    }
    public OnlineOrder getOnlineOrder(String pid){
        OnlineOrder order = new OnlineOrder();
        switch (Integer.parseInt(pid)){

        }
        return order;
    }
    public JSONObject _getResult(OnlineOrder order, Users user){
        OnlinePay onlinePay = onlinePayDao.findAllById(order.getPid());
        JSONObject object = new JSONObject();
        object.put("state","error");
        if (onlinePay.getTitle().contains("钻石")){
            if (user.getDiamond() >= order.getAmount()){
                _handlerVipStatus(order.getOrderNo());
                order.setStatus(1);
                user.setDiamond(order.getAmount());
                userService._saveAndPush(user);
                onlineOrderDao.saveAndFlush(order);
                object.put("state","ok");
            }else {
                object.put("msg","余额不足，请选择其他方式!");
            }
        }else {

        }

        return object;
    }
    public void _handlerVipStatus(String orderId){
        CommodityVipOrder order = commodityVipOrderDao.findAllByOrderId(orderId);
        if (order != null){
            order.setStatus(1);
            commodityVipOrderDao.saveAndFlush(order);
            Users user = userService._getById(order.getUid());
            CommodityVip commodityVip = commodityVipDao.findAllById(order.getCid());
            commodityVipOrderService._handlerAddTime(user,commodityVip);
        }
    }
    public JSONObject _postCrateOrder(Users user,String type, String order_id, String pid) {
        OnlineOrder order = new OnlineOrder();
        order.setOrderId(TimeUtil._getOrderNo()+user.getId());
        order.setCtime(System.currentTimeMillis() / 1000L);
        order.setUtime(System.currentTimeMillis() / 1000L);
        order.setUid(user.getId());
        order.setStatus(0);
        order.setPid(Integer.parseInt(pid));
        order.setOrderNo(order_id);
        order.setType(Integer.parseInt(type));
        switch (Integer.parseInt(type)){
            case PAY_ONLINE_VIP:
                CommodityVipOrder commodityVipOrder = commodityVipOrderDao.findAllByOrderId(order_id);
                if (commodityVipOrder != null){
                    order.setAmount(commodityVipOrder.getAmount());
                }
                break;
            case PAY_ONLINE_GOLD:
                break;
            case PAY_ONLINE_DIAMOND:
                break;
            default:
                break;
        }
        return _getResult(order,user);
    }
    private JSONArray _getList(List<OnlineOrder> orders){
        JSONArray array = new JSONArray();
        for (OnlineOrder order: orders) {
            JSONObject object = new JSONObject();
            object.put("id",order.getId());
            object.put("type" ,order.getType());
            object.put("amount",order.getAmount());
            object.put("ctime",order.getCtime());
            object.put("utime",order.getUtime());
            object.put("status",order.getStatus());
            object.put("orderId",order.getOrderId());
            object.put("orderNo",order.getOrderNo());
            OnlinePay onlinePay = onlinePayDao.findAllById(order.getPid());
            if (onlinePay != null){
                object.put("onlinePay",onlinePay);
            }
            array.add(object);
        }
        return array;
    }
    public ResultData _getOrders(Users user, ResultData data, String pages) {
        int page = 0;
        if (StringUtils.isNotEmpty(pages)){
            page = (Integer.parseInt(pages) - 1);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<OnlineOrder> onlineOrders = onlineOrderDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list",_getList(onlineOrders.getContent()));
        object.put("total",onlineOrders.getTotalPages());
        data.setData(object);
        return data;
    }
}
