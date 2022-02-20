package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.CommodityVipDao;
import com.telebott.moviejava.dao.OnlineOrderDao;
import com.telebott.moviejava.dao.OnlinePayDao;
import com.telebott.moviejava.entity.CommodityVip;
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
    public void _save(OnlineOrder onlineOrder){
        onlineOrderDao.saveAndFlush(onlineOrder);
    }
    public JSONObject getResult(OnlineOrder onlineOrder){
        return new JSONObject();
    }

    public JSONObject _getOrder(Users user, String type, String amount, String cid,String id) {
        JSONObject object = new JSONObject();
        switch (Integer.parseInt(type)){
            case PAY_ONLINE_VIP:
                CommodityVip commodityVip = commodityVipDao.findAllById(Long.parseLong(cid));
                if (commodityVip != null){

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
