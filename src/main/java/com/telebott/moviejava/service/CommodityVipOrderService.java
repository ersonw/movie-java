package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.CommodityVipOrderDao;
import com.telebott.moviejava.dao.UsersDao;
import com.telebott.moviejava.entity.CommodityVip;
import com.telebott.moviejava.entity.CommodityVipOrder;
import com.telebott.moviejava.entity.Users;
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
public class CommodityVipOrderService {
    @Autowired
    private CommodityVipOrderDao commodityVipOrderDao;
    @Autowired
    private CommodityVipDao commodityVipDao;
    @Autowired
    private UserService userService;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    public void _save(CommodityVipOrder commodityVipOrder){
        commodityVipOrderDao.saveAndFlush(commodityVipOrder);
    }
    public JSONObject _crateOrder(Users user, String id){
        JSONObject object = new JSONObject();
        List<CommodityVipOrder> orderList = commodityVipOrderDao.findAllByUidAndStatus(user.getId(),0);
        CommodityVip commodityVip = commodityVipDao.findAllById(Long.parseLong(id));
        if (orderList.size() > 0){
            object.put("crate", false);
            object.put("id",orderList.get(0).getOrderId());
        }else if (commodityVip != null){
            long time = System.currentTimeMillis();
            CommodityVipOrder order = new CommodityVipOrder();
            order.setUid(user.getId());
            order.setCid(commodityVip.getId());
            order.setAmount(commodityVip.getAmount());
            order.setCtime(time);
            order.setStatus(0);
            order.setOrderId(time +String.valueOf(user.getId()));
            commodityVipOrderDao.saveAndFlush(order);
            object.put("crate", true);
            object.put("id",order.getOrderId());
        }
        return object;
    }
    public void _handlerAddTime(Users user, CommodityVip commodityVip) {
        Users _user = authDao.findUserByIdentifier(user.getIdentifier());
        user.setToken(_user.getToken());
        long time = _getAddTime(commodityVip.getAddTime(),user.getExpireds());
        user.setExpireds(time);
//        userService._saveAndPush(user);
        System.out.println(user);
        usersDao.saveAndFlush(user);
//        authDao.pushUser(user);
    }
    public static long _getAddTime(String time, long e){
        long t = e>0 ? e - System.currentTimeMillis() : 0;
        return _getAddTime(time) + t;
    }
    private static long _getAddTime(String time){
        if (StringUtils.isEmpty(time)){
            return TimeUtil.manyDaysLater(365 * 99);
        }
        if (time.contains("d") || time.contains("D")){
            time = time.replaceAll("d","").replaceAll("D","");
            if (Integer.parseInt(time) > 0){
                return TimeUtil.manyDaysLater(Integer.parseInt(time));
            }
            return TimeUtil.manyDaysLater(1);
        }else if(time.contains("m") || time.contains("M")){
            time = time.replaceAll("m","").replaceAll("M","");
            if (Integer.parseInt(time) > 0){
                return TimeUtil.manyDaysLater(Integer.parseInt(time) * 30);
            }
            return TimeUtil.manyDaysLater(30);
        }else if(time.contains("y") || time.contains("Y")){
            time = time.replaceAll("y","").replaceAll("Y","");
            if (Integer.parseInt(time) > 0){
                return TimeUtil.manyDaysLater(Integer.parseInt(time) * 365);
            }
            return TimeUtil.manyDaysLater(365);
        }
        return 0;
    }
    public JSONObject _getOrder(Users user, String data) {
        JSONObject objectData = JSONObject.parseObject(data);
        int page = 0;
        if (objectData.get("page") != null){
            page = (Integer.parseInt(objectData.get("page").toString()) - 1);
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<CommodityVipOrder> vipOrders = commodityVipOrderDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list",_getList(vipOrders.getContent()));
        object.put("total",vipOrders.getTotalPages());
        return object;
    }
    private JSONArray _getList(List<CommodityVipOrder> content) {
        JSONArray array = new JSONArray();
        for (CommodityVipOrder order: content) {
            JSONObject object = new JSONObject();
            object.put("id",order.getId());
            object.put("ctime",order.getCtime());
            object.put("orderId",order.getOrderId());
            object.put("status", order.getStatus());
            object.put("amount", order.getAmount());
            CommodityVip vip = commodityVipDao.findAllById(order.getCid());
            if (vip != null){
                object.put("vipBuy",_getVipBuy(vip));
            }
            array.add(object);
        }
        return array;
    }
    private JSONObject _getVipBuy(CommodityVip vip){
        JSONObject object = new JSONObject();
        object.put("id",vip.getId());
        object.put("amount",vip.getAmount());
        object.put("title",vip.getTitle());
        object.put("describes",vip.getDescribes());
        object.put("currency",vip.getCurrency());
        return object;
    }

    public JSONObject _cancelOrder(Users user, String id) {
        JSONObject object = new JSONObject();
        CommodityVipOrder order = commodityVipOrderDao.findAllByIdAndUid(Long.parseLong(id),user.getId());
        if (order != null && order.getStatus() == 0){
            order.setStatus(-1);
            commodityVipOrderDao.saveAndFlush(order);
        }
        object.put("state","ok");
        return object;
    }
}
