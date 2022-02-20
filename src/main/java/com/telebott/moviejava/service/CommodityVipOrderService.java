package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.CommodityVipOrderDao;
import com.telebott.moviejava.entity.CommodityVip;
import com.telebott.moviejava.entity.CommodityVipOrder;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityVipOrderService {
    @Autowired
    private CommodityVipOrderDao commodityVipOrderDao;
    @Autowired
    private CommodityVipDao commodityVipDao;
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
            order.setCtime(time);
            order.setExpired(_getAddTime(commodityVip.getAddTime()));
            order.setStatus(0);
            order.setOrderId(time +String.valueOf(user.getId()));
            commodityVipOrderDao.saveAndFlush(order);
            object.put("crate", true);
            object.put("id",order.getOrderId());
        }
        return object;
    }
    private long _getAddTime(String time){
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
}
