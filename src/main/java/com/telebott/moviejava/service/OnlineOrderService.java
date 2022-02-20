package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.CommodityVipOrderDao;
import com.telebott.moviejava.dao.OnlineOrderDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.CommodityVip;
import com.telebott.moviejava.entity.CommodityVipOrder;
import com.telebott.moviejava.entity.OnlineOrder;
import com.telebott.moviejava.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnlineOrderService {
    private static final int PAY_ONLINE_VIP = 100;
    private static final int PAY_ONLINE_GOLD = 101;
    private static final int PAY_ONLINE_DIAMOND = 102;
    @Autowired
    private OnlineOrderDao onlineOrderDao;
    @Autowired
    private CommodityVipDao commodityVipDao;
    @Autowired
    private CommodityVipOrderDao commodityVipOrderDao;
    public void _save(OnlineOrder onlineOrder){
        onlineOrderDao.saveAndFlush(onlineOrder);
    }
    public JSONObject getResult(OnlineOrder onlineOrder){
        return new JSONObject();
    }
    public JSONObject getResult(CommodityVip commodityVip){
        JSONObject object = new JSONObject();
        object.put("title",commodityVip.getTitle());
        object.put("describes",commodityVip.getDescribes());
        object.put("currency",commodityVip.getCurrency());
        object.put("amount",commodityVip.getAmount());
        return object;
    }

    public JSONObject _getOrder(String type, String order_id) {
        JSONObject object = new JSONObject();
        switch (Integer.parseInt(type)){
            case PAY_ONLINE_VIP:
                CommodityVipOrder commodityVipOrder = commodityVipOrderDao.findAllByOrderId(order_id);
                if (commodityVipOrder != null){
                    CommodityVip commodityVip = commodityVipDao.findAllById(commodityVipOrder.getCid());
                    if (commodityVip != null){
                        object = getResult(commodityVip);
                    }
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
}
