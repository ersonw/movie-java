package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.ShowPayUtil;
import com.telebott.moviejava.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OnlineOrderService {
    private static final int PAY_ONLINE_VIP = 100;
    private static final int PAY_ONLINE_GOLD = 101;
    private static final int PAY_ONLINE_DIAMOND = 102;
    private static int PAY_MCH_INDEX = 0;
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
    @Autowired
    private BalanceOrdersDao balanceOrdersDao;
    @Autowired
    private ShowPayOrdersDao showPayOrdersDao;
    @Autowired
    private ShowPayDao showPayDao;
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

    public JSONObject _getResult(OnlineOrder order, Users user){
        OnlinePay onlinePay = onlinePayDao.findAllById(order.getPid());
        JSONObject object = new JSONObject();
        object.put("state","error");
        if (onlineOrderDao.countAllByOrderNoAndStatus(order.getOrderNo(),0) > 0){
            object.put("msg", "重复订单号！上个订单未处理完成");
            return object;
        }
        if (onlinePay.getType() == 0){
            long balance = balanceOrdersDao.countAllByBalance(user.getId());
            if (order.getAmount() < balance){
                BalanceOrders balanceOrders = new BalanceOrders();
                balanceOrders.setAmount(-order.getAmount());
                balanceOrders.setAddTime(System.currentTimeMillis());
                balanceOrders.setStatus(1);
                balanceOrders.setUid(user.getId());
                balanceOrders.setUpdateTime(System.currentTimeMillis());
                order.setStatus(1);
                onlineOrderDao.saveAndFlush(order);
                object.put("state", "ok");
                user.setUtime(System.currentTimeMillis());
                switch (order.getType()) {
                    case PAY_ONLINE_VIP:
                        CommodityVipOrder orders = commodityVipOrderDao.findAllByOrderId(order.getOrderNo());
                        if (orders != null){
                            CommodityVip commodityVip = commodityVipDao.findAllById(orders.getCid());
                            if (commodityVip != null){
                                orders.setStatus(1);
                                commodityVipOrderDao.saveAndFlush(orders);
                                long time = CommodityVipOrderService._getAddTime(commodityVip.getAddTime(), user.getExpireds());
                                user.setExpireds(time);
                                userService._saveAndPush(user);
                                onlineOrderDao.saveAndFlush(order);
                                balanceOrders.setReason("购买了价值￥" + (order.getAmount() / 100) + "的会员");
                                balanceOrdersDao.saveAndFlush(balanceOrders);
                            }
                        }
                        break;
                    case PAY_ONLINE_GOLD:
                        CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order.getOrderNo());
                        if (commodityGoldOrder != null){
                            CommodityGold commodityGold = commodityGoldDao.findAllById(commodityGoldOrder.getCid());
                            if (commodityGold != null){
                                commodityGoldOrder.setStatus(1);
                                commodityGoldOrderDao.saveAndFlush(commodityGoldOrder);
                                user.setGold(user.getGold() + commodityGold.getGold());
                                GoldRecords goldRecords = new GoldRecords();
                                goldRecords.setGold(commodityGold.getGold());
                                goldRecords.setAddTime(System.currentTimeMillis());
                                goldRecords.setUid(user.getId());
                                goldRecords.setStatus(1);
                                goldRecords.setReason("通过 "+ onlinePay.getTitle() +"的在线支付购买了金币");
                                goldRecordsDao.saveAndFlush(goldRecords);
                                userService._saveAndPush(user);
                                balanceOrders.setReason("购买了价值￥"+(order.getAmount() / 100)+"的金币");
                                balanceOrdersDao.saveAndFlush(balanceOrders);
                            }
                        }
                        break;
                    case PAY_ONLINE_DIAMOND:
                        CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order.getOrderNo());
                        if (commodityDiamondOrder != null){
                            CommodityDiamond commodityDiamond = commodityDiamondDao.findAllById(commodityDiamondOrder.getCid());
                            if (commodityDiamond != null){
                                commodityDiamondOrder.setStatus(1);
                                commodityDiamondOrderDao.saveAndFlush(commodityDiamondOrder);
                                user.setDiamond(user.getDiamond()+ commodityDiamond.getDiamond());
                                DiamondRecords diamondRecords = new DiamondRecords();
                                diamondRecords.setDiamond(commodityDiamond.getDiamond());
                                diamondRecords.setReason("通过 "+ onlinePay.getTitle() +"的在线支付购买了钻石");
                                diamondRecords.setUid(user.getId());
                                diamondRecords.setAddTime(System.currentTimeMillis());
                                diamondRecords.setStatus(1);
                                diamondRecordsDao.saveAndFlush(diamondRecords);
                                userService._saveAndPush(user);
                                balanceOrders.setReason("购买了价值￥"+(order.getAmount() / 100)+"的钻石");
                                balanceOrdersDao.saveAndFlush(balanceOrders);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }else {
                object.put("msg","余额不足，请选择在线支付!");
            }
        }else {
            ShowPayOrders showPayOrders = new ShowPayOrders();
            showPayOrders.setOrderNo(order.getOrderId());
            showPayOrders.setStatus(0);
            showPayOrders.setAmount(order.getAmount());
            showPayOrders.setAddTime(System.currentTimeMillis());
            String url = getPostOrder(showPayOrders, onlinePay.getType());
            if (url != null){
                object.put("url",url);
                object.put("state","ok");
                onlineOrderDao.saveAndFlush(order);
            }
        }
        return object;
    }
    public String getPostOrder(ShowPayOrders showPayOrders, long pid){
        ShowPay showPay = null;
        if (pid > 0){
            showPay = showPayDao.findAllById(pid);
        }else {
            List<ShowPay> showPays = showPayDao.findAll();
            if (showPays.size() > PAY_MCH_INDEX){
                showPay = showPays.get(PAY_MCH_INDEX);
                PAY_MCH_INDEX++;
                if (PAY_MCH_INDEX == showPays.size()){
                    PAY_MCH_INDEX = 0;
                }
            }else {
                PAY_MCH_INDEX = 0;
            }
        }
        if (showPay == null) return null;
        String url = ShowPayUtil.toPay(showPayOrders,showPay);
        if (url != null){
            showPayOrdersDao.saveAndFlush(showPayOrders);
            return url;
        }
        return null;
    }
    public boolean handlerToPayNotify(ToPayNotify toPayNotify){
        ShowPay showPay = showPayDao.findAllByMchId(toPayNotify.getMchid());
        if (showPay == null) return false;
        if (!ShowPayUtil.toPayNotify(toPayNotify,showPay)) return false;
        ShowPayOrders showPayOrders = showPayOrdersDao.findAllByOrderNo(toPayNotify.getOut_trade_no());
        if (showPayOrders != null && StringUtils.isEmpty(showPayOrders.getTradeNo()) && showPayOrders.getStatus() != 1){
            showPayOrders.setTradeNo(toPayNotify.getTrade_no());
            showPayOrders.setStatus(1);
            showPayOrdersDao.saveAndFlush(showPayOrders);
            handlerOrderNotify(showPayOrders);
        }
        return true;
    }
    private void handlerOrderNotify(ShowPayOrders showPayOrders){
        OnlineOrder order = onlineOrderDao.findAllByOrderId(showPayOrders.getOrderNo());
        if (order != null){
            OnlinePay onlinePay = onlinePayDao.findAllById(order.getPid());
            Users user = usersDao.findAllById(order.getUid());
            order.setStatus(1);
            onlineOrderDao.saveAndFlush(order);
            user.setUtime(System.currentTimeMillis());
            switch (order.getType()) {
                case PAY_ONLINE_VIP:
                    CommodityVipOrder orders = commodityVipOrderDao.findAllByOrderId(order.getOrderNo());
                    if (orders != null){
                        CommodityVip commodityVip = commodityVipDao.findAllById(orders.getCid());
                        if (commodityVip != null){
                            orders.setStatus(1);
                            commodityVipOrderDao.saveAndFlush(orders);
                            long time = CommodityVipOrderService._getAddTime(commodityVip.getAddTime(), user.getExpireds());
                            user.setExpireds(time);
                            userService._saveAndPush(user);
                            onlineOrderDao.saveAndFlush(order);
                        }
                    }
                    break;
                case PAY_ONLINE_GOLD:
                    CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order.getOrderNo());
                    if (commodityGoldOrder != null){
                        CommodityGold commodityGold = commodityGoldDao.findAllById(commodityGoldOrder.getCid());
                        if (commodityGold != null){
                            commodityGoldOrder.setStatus(1);
                            commodityGoldOrderDao.saveAndFlush(commodityGoldOrder);
                            user.setGold(user.getGold() + commodityGold.getGold());
                            GoldRecords goldRecords = new GoldRecords();
                            goldRecords.setGold(commodityGold.getGold());
                            goldRecords.setAddTime(System.currentTimeMillis());
                            goldRecords.setUid(user.getId());
                            goldRecords.setStatus(1);
                            goldRecords.setReason("通过 "+ onlinePay.getTitle() +"的在线支付购买了金币");
                            goldRecordsDao.saveAndFlush(goldRecords);
                            userService._saveAndPush(user);
                        }
                    }
                    break;
                case PAY_ONLINE_DIAMOND:
                    CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order.getOrderNo());
                    if (commodityDiamondOrder != null){
                        CommodityDiamond commodityDiamond = commodityDiamondDao.findAllById(commodityDiamondOrder.getCid());
                        if (commodityDiamond != null){
                            commodityDiamondOrder.setStatus(1);
                            commodityDiamondOrderDao.saveAndFlush(commodityDiamondOrder);
                            user.setDiamond(user.getDiamond()+ commodityDiamond.getDiamond());
                            DiamondRecords diamondRecords = new DiamondRecords();
                            diamondRecords.setDiamond(commodityDiamond.getDiamond());
                            diamondRecords.setReason("通过 "+ onlinePay.getTitle() +"的在线支付购买了钻石");
                            diamondRecords.setUid(user.getId());
                            diamondRecords.setAddTime(System.currentTimeMillis());
                            diamondRecords.setStatus(1);
                            diamondRecordsDao.saveAndFlush(diamondRecords);
//                            userService._saveAndPush(user);
                        }
                    }
                    break;
                default:
                    break;
            }
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
        switch (order.getType()){
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
