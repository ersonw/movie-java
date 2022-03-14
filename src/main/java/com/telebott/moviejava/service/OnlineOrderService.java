package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
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
    @Autowired
    private CommodityDiamondOrderDao commodityDiamondOrderDao;
    @Autowired
    private CommodityDiamondDao commodityDiamondDao;
    @Autowired
    private DiamondRecordsDao diamondRecordsDao;
    @Autowired
    private GoldRecordsDao goldRecordsDao;
    @Autowired
    private CommodityGoldDao commodityGoldDao;
    @Autowired
    private  CommodityGoldOrderDao commodityGoldOrderDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private UsersDao usersDao;
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
    public JSONObject getResult(CommodityDiamond commodityDiamond){
        JSONObject object = new JSONObject();
        object.put("title",commodityDiamond.getDiamond()+"钻石");
        object.put("currency","￥");
        object.put("describes", "购买"+commodityDiamond.getDiamond()+"钻石");
        return object;
    }
    public JSONObject getResult(CommodityGold commodityGold){
        JSONObject object = new JSONObject();
        object.put("title",commodityGold.getGold()+"金币");
        object.put("currency","￥");
        object.put("describes", "购买"+commodityGold.getGold()+"金币");
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
                CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order_id);
                if (commodityGoldOrder != null){
                    CommodityGold commodityGold = commodityGoldDao.findAllById(commodityGoldOrder.getCid());
                    object = getResult(commodityGold);
                    object.put("amount", commodityGoldOrder.getAmount());
                }
                break;
            case PAY_ONLINE_DIAMOND:
                CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order_id);
                if (commodityDiamondOrder != null){
                    CommodityDiamond commodityDiamond = commodityDiamondDao.findAllById(commodityDiamondOrder.getCid());
                    object = getResult(commodityDiamond);
                    object.put("amount", commodityDiamondOrder.getAmount());
                }
                break;
            default:
                break;
        }
        return object;
    }

    public JSONObject _getResult(OnlineOrder order, Users user, int type){
        OnlinePay onlinePay = onlinePayDao.findAllById(order.getPid());
        JSONObject object = new JSONObject();
        object.put("state","error");
        object.put("msg","");
        if (onlinePay.getTitle().contains("钻石")){
            if (user.getDiamond() >= order.getAmount()){
                DiamondRecords diamondRecords = new DiamondRecords();
                diamondRecords.setDiamond((-order.getAmount()));
                diamondRecords.setUid(user.getId());
                diamondRecords.setCtime(System.currentTimeMillis());
                switch (type){
                    case PAY_ONLINE_VIP:
//                        _handlerVipStatus(order.getOrderNo());
                        order.setStatus(1);
                        user.setDiamond(user.getDiamond() - order.getAmount());
                        CommodityVipOrder orders = commodityVipOrderDao.findAllByOrderId(order.getOrderNo());
                        orders.setStatus(1);
                        commodityVipOrderDao.saveAndFlush(orders);
                        CommodityVip commodityVip = commodityVipDao.findAllById(orders.getCid());
                        long time = CommodityVipOrderService._getAddTime(commodityVip.getAddTime(),user.getExpireds());
                        user.setExpireds(time);
                        user.setUtime(System.currentTimeMillis() / 1000);
                        userService._saveAndPush(user);
                        onlineOrderDao.saveAndFlush(order);
                        object.put("state","ok");
                        diamondRecords.setReason("兑换价值￥"+(order.getAmount() /100)+"的会员");
                        diamondRecordsDao.saveAndFlush(diamondRecords);
                        break;
                    case PAY_ONLINE_GOLD:
                        object.put("state","ok");
                        diamondRecords.setReason("兑换价值￥"+(order.getAmount() / 100)+"的金币");
                        diamondRecordsDao.saveAndFlush(diamondRecords);
                        order.setStatus(1);
                        user.setDiamond(user.getDiamond() - order.getAmount());
                        CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order.getOrderNo());
                        if (commodityGoldOrder != null){
                            commodityGoldOrder.setStatus(1);
                            commodityGoldOrderDao.saveAndFlush(commodityGoldOrder);
                            CommodityGold commodityGold = commodityGoldDao.findAllById(commodityGoldOrder.getCid());
                            if (commodityGold != null){
                                user.setGold(user.getGold() + commodityGold.getGold());
                                GoldRecords goldRecords = new GoldRecords();
                                goldRecords.setGold(commodityGold.getGold());
                                goldRecords.setCtime(System.currentTimeMillis());
                                goldRecords.setUid(user.getId());
                                goldRecords.setReason("使用"+order.getAmount()+"的钻石兑换了金币");
                                goldRecordsDao.saveAndFlush(goldRecords);
                            }
                        }
                        userService._saveAndPush(user);
                        break;
                    case PAY_ONLINE_DIAMOND:
                        object.put("msg","不支持钻石兑换钻石，请选择在线支付!");
                        break;
                    default:
                        break;
                }
            }else {
                object.put("msg","余额不足，请选择在线支付!");
            }
        }else {
//            order.setStatus(1);
//            onlineOrderDao.saveAndFlush(order);
//            CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order.getOrderNo());
//            if (commodityDiamondOrder != null){
//                commodityDiamondOrder.setStatus(1);
//                commodityDiamondOrderDao.saveAndFlush(commodityDiamondOrder);
//            }
//            DiamondRecords diamondRecords = new DiamondRecords();
//            diamondRecords.setDiamond((+order.getAmount()));
//            diamondRecords.setUid(user.getId());
//            diamondRecords.setCtime(System.currentTimeMillis());
//            diamondRecords.setReason("充值价值￥"+(order.getAmount() /100)+"的钻石");
//            diamondRecordsDao.saveAndFlush(diamondRecords);
//            user.setDiamond(user.getDiamond()+order.getAmount());
//            userService._saveAndPush(user);
//            object.put("state","ok");
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
//            commodityVipOrderService._handlerAddTime(user,commodityVip);
            Users _user = authDao.findUserByIdentifier(user.getIdentifier());
            user.setToken(_user.getToken());
            long time = CommodityVipOrderService._getAddTime(commodityVip.getAddTime(),user.getExpireds());
            user.setExpireds(time);
            user.setUtime(System.currentTimeMillis() / 1000);
            usersDao.save(user);
            System.out.println(user);
//            userService._saveAndPush(user);
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
                CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order_id);
                if (commodityGoldOrder != null){
                    order.setAmount(commodityGoldOrder.getAmount());
                }
                break;
            case PAY_ONLINE_DIAMOND:
                CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order_id);
                if (commodityDiamondOrder != null){
                    order.setAmount(commodityDiamondOrder.getAmount());
                }
                break;
            default:
                break;
        }
        return _getResult(order,user,Integer.parseInt(type));
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
