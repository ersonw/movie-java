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
import org.springframework.data.relational.core.sql.In;
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
    private static final int WITHDRAWAL_DIAMOND = 100;
    private static final int WITHDRAWAL_BALANCE = 101;
    private static final int WITHDRAWAL_GOLD = 102;
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
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private WithdrawalRecordsDao withdrawalRecordsDao;
    @Autowired
    private WithdrawalCardsDao withdrawalCardsDao;

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
                    if (commodityVip == null){
                        object.put("state","error");
                        object.put("msg", "商品不存在或者已下架!");
                        return object;
                    }
                    object = getResult(commodityVip);
                    object.put("amount",commodityVipOrder.getAmount());
                }else{
                    object.put("state","error");
                    object.put("msg", "订单不存在或者已过期");
                    return object;
                }
                break;
            case PAY_ONLINE_GOLD:
                CommodityGoldOrder commodityGoldOrder = commodityGoldOrderDao.findAllByOrderId(order_id);
                if (commodityGoldOrder != null){
                    CommodityGold commodityGold = commodityGoldDao.findAllById(commodityGoldOrder.getCid());
                    if (commodityGold == null){
                        object.put("state","error");
                        object.put("msg", "商品不存在或者已下架!");
                        return object;
                    }
                    object = getResult(commodityGold);
                    object.put("amount", commodityGoldOrder.getAmount());
                }else{
                    object.put("state","error");
                    object.put("msg", "订单不存在或者已过期");
                    return object;
                }
                break;
            case PAY_ONLINE_DIAMOND:
                CommodityDiamondOrder commodityDiamondOrder = commodityDiamondOrderDao.findAllByOrderId(order_id);
                if (commodityDiamondOrder != null){
                    CommodityDiamond commodityDiamond = commodityDiamondDao.findAllById(commodityDiamondOrder.getCid());
                    if (commodityDiamond == null){
                        object.put("state","error");
                        object.put("msg", "商品不存在或者已下架!");
                        return object;
                    }
                    object = getResult(commodityDiamond);
                    object.put("amount", commodityDiamondOrder.getAmount());
                }else{
                    object.put("state","error");
                    object.put("msg", "订单不存在或者已过期");
                    return object;
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
//                System.out.println(url);
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
            JSONObject object = JSONObject.parseObject(JSONObject.toJSONString(order));
//            object.put("id",order.getId());
//            object.put("type" ,order.getType());
//            object.put("amount",order.getAmount());
//            object.put("ctime",order.getCtime());
//            object.put("utime",order.getUtime());
//            object.put("status",order.getStatus());
//            object.put("orderId",order.getOrderId());
//            object.put("orderNo",order.getOrderNo());
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
        if (page < 0) page = 0;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, 50, sort);
        Page<OnlineOrder> onlineOrders = onlineOrderDao.findAllByUid(user.getId(),pageable);
        JSONObject object = new JSONObject();
        object.put("list",_getList(onlineOrders.getContent()));
        object.put("total",onlineOrders.getTotalPages());
        data.setData(object);
//        System.out.println(data.getData());
        return data;
    }

    public JSONObject getWithdrawal(Users user) {
        JSONObject object = new JSONObject();
        object.put("proportionBalance", Integer.parseInt(systemConfigService.getValueByKey("proportionBalance")));
        object.put("proportionDiamond", Integer.parseInt(systemConfigService.getValueByKey("proportionDiamond")));
        object.put("proportionGold", Integer.parseInt(systemConfigService.getValueByKey("proportionGold")));
        object.put("MaxWithdrawal", Integer.parseInt(systemConfigService.getValueByKey("MaxWithdrawal")));
        object.put("MiniWithdrawal", Integer.parseInt(systemConfigService.getValueByKey("MiniWithdrawal")));
        object.put("cards", withdrawalCardsDao.findAllByUid(user.getId()));
        return object;
    }

    public JSONObject getWithdrawalRecords(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        int page = 1;
        if (data != null && data.get("page") != null) page = Integer.parseInt(data.getString("page"));
        page--;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<WithdrawalRecords> withdrawalRecordsPage = withdrawalRecordsDao.findAllByUid(user.getId(),pageable);
        for (WithdrawalRecords record: withdrawalRecordsPage.getContent()) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(record));
            jsonObject.put("uid","");
            array.add(jsonObject);
        }
        object.put("total",withdrawalRecordsPage.getTotalPages());
        object.put("list",array);
        return object;
    }

    public JSONObject Withdrawal(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (data != null &&
                data.get("amount") != null && data.get("card") != null &&
                data.get("type") != null && StringUtils.isNotEmpty(data.getString("amount")) &&
                    StringUtils.isNotEmpty(data.getString("card")) &&
                    StringUtils.isNotEmpty(data.getString("type"))){
            WithdrawalCards cards = withdrawalCardsDao.findAllById(Long.parseLong(data.getString("card")));
            if (cards != null){
                long balance = 0;
                long amount = Long.parseLong(data.getString("amount"));
                int proportionBalance = Integer.parseInt(systemConfigService.getValueByKey("proportionBalance"));
                int proportionDiamond = Integer.parseInt(systemConfigService.getValueByKey("proportionDiamond"));
                int proportionGold = Integer.parseInt(systemConfigService.getValueByKey("proportionGold"));
                int MaxWithdrawal = Integer.parseInt(systemConfigService.getValueByKey("MaxWithdrawal"));
                int MiniWithdrawal = Integer.parseInt(systemConfigService.getValueByKey("MiniWithdrawal"));
                WithdrawalRecords records = new WithdrawalRecords();
                records.setAddTime(System.currentTimeMillis());
                records.setUpdateTime(System.currentTimeMillis());
                records.setOrderNo(TimeUtil._getOrderNo());
                records.setStatus(0);
                records.setUid(user.getId());
                records.setCid(cards.getId());
                System.out.println(((amount * 100) * (proportionBalance / 100d)));
                switch (Integer.parseInt(data.getString("type"))){
                    case WITHDRAWAL_BALANCE:
                        balance = balanceOrdersDao.countAllByBalance(user.getId());
                        if (amount > balance){
                            object.put("msg","提现金额不得大于剩余余额！");
                        }else if(((amount * 100) * (proportionBalance / 100d)) > (MaxWithdrawal)){
                            object.put("msg","单笔提现金额不得大于最大提现额度 ￥"+String.format("%.2f",MaxWithdrawal / 100d)+"！");
                        }else if(((amount * 100) * (proportionBalance / 100d)) < (MiniWithdrawal)){
                            object.put("msg","单笔提现金额不得少于最小提现额度 ￥"+String.format("%.2f",MiniWithdrawal / 100d)+"！");
                        }else {
                            object.put("verify",true);
                            records.setAmount(new Double((amount * 100) * (proportionBalance / 100d)).longValue());
                            records.setReason("余额提现");
                            withdrawalRecordsDao.saveAndFlush(records);
                            BalanceOrders balanceOrders = new BalanceOrders();
                            balanceOrders.setReason(records.getReason());
                            balanceOrders.setStatus(1);
                            balanceOrders.setAmount(-(amount * 100));
                            balanceOrders.setAddTime(System.currentTimeMillis());
                            balanceOrders.setUpdateTime(System.currentTimeMillis());
                            balanceOrders.setUid(user.getId());
                            balanceOrdersDao.saveAndFlush(balanceOrders);
                        }
                        break;
                    case WITHDRAWAL_DIAMOND:
                        balance = diamondRecordsDao.countAllByBalance(user.getId());
                        if (amount > balance){
                            object.put("msg","提现金额不得大于剩余余额！");
                        }else if((amount / proportionDiamond) > (MaxWithdrawal / 100)){
                            object.put("msg","单笔提现金额不得大于最大提现额度 ￥"+String.format("%.2f",MaxWithdrawal / 100d)+"！");
                        }else if((amount / proportionDiamond) < (MiniWithdrawal / 100)){
                            object.put("msg","单笔提现金额不得少于最小提现额度 ￥"+String.format("%.2f",MiniWithdrawal / 100d)+"！");
                        }else {
                            object.put("verify",true);
                            records.setAmount((amount / proportionDiamond) * 100);
                            records.setReason("钻石提现");
                            withdrawalRecordsDao.saveAndFlush(records);
                            DiamondRecords diamondRecords = new DiamondRecords();
                            diamondRecords.setReason(records.getReason());
                            diamondRecords.setStatus(1);
                            diamondRecords.setDiamond(-amount);
                            diamondRecords.setAddTime(System.currentTimeMillis());
                            diamondRecords.setUpdateTime(System.currentTimeMillis());
                            diamondRecords.setUid(user.getId());
                            diamondRecordsDao.saveAndFlush(diamondRecords);
                        }
                        break;
                    case WITHDRAWAL_GOLD:
                        balance = goldRecordsDao.countAllByBalance(user.getId());
                        if (amount > balance){
                            object.put("msg","提现金额不得大于剩余余额！");
                        }else if((amount / proportionGold) > (MaxWithdrawal / 100)){
                            object.put("msg","单笔提现金额不得大于最大提现额度 ￥"+String.format("%.2f",MaxWithdrawal / 100d)+"！");
                        }else if((amount / proportionGold) < (MiniWithdrawal / 100)){
                            object.put("msg","单笔提现金额不得少于最小提现额度 ￥"+String.format("%.2f",MiniWithdrawal / 100d)+"！");
                        }else {
                            object.put("verify",true);
                            records.setAmount((amount / proportionGold) * 100);
                            records.setReason("金币提现");
                            withdrawalRecordsDao.saveAndFlush(records);
                            GoldRecords goldRecords = new GoldRecords();
                            goldRecords.setReason(records.getReason());
                            goldRecords.setStatus(1);
                            goldRecords.setGold(-amount);
                            goldRecords.setAddTime(System.currentTimeMillis());
                            goldRecords.setUpdateTime(System.currentTimeMillis());
                            goldRecords.setUid(user.getId());
                            goldRecordsDao.saveAndFlush(goldRecords);
                        }
                        break;
                }
            }else {
                object.put("msg","卡号不存在!");
            }
        }
        return object;
    }

    public JSONObject addCard(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (data != null &&
                data.get("name") != null && data.get("bank") != null && data.get("code") != null ){
            if (StringUtils.isNotEmpty(data.getString("name")) &&
                    StringUtils.isNotEmpty(data.getString("bank")) &&
                    StringUtils.isNotEmpty(data.getString("code"))){
                WithdrawalCards cards = withdrawalCardsDao.findAllByCode(data.getString("code"));
                if (cards != null){
                    object.put("msg","卡号已存在!");
                }else {
                    if (data.get("id") != null && Long.parseLong(data.getString("id")) > 0){
                        object.put("msg","暂不支持自主修改，需要修改请联系在线客服!");
//                        cards = withdrawalCardsDao.findAllByIdAndUid(Long.parseLong(data.getString("id")), user.getId());
//                        if (cards == null){
//                            object.put("msg","收款方式不存在!");
//                        }else {
//                            cards.setUpdateTime(System.currentTimeMillis());
//                            cards.setName(data.getString("name"));
//                            cards.setBank(data.getString("bank"));
//                            cards.setCode(data.getString("code"));
//                            withdrawalCardsDao.saveAndFlush(cards);
//                            object.put("verify",true);
//                        }
                    }else {
                        cards =new WithdrawalCards();
                        cards.setUpdateTime(System.currentTimeMillis());
                        cards.setAddTime(System.currentTimeMillis());
                        cards.setName(data.getString("name"));
                        cards.setBank(data.getString("bank"));
                        cards.setCode(data.getString("code"));
                        cards.setUid(user.getId());
                        withdrawalCardsDao.saveAndFlush(cards);
                        object.put("verify",true);
                    }
                }
            }else {
                object.put("msg","各项都是必填参数，不可或缺!");
            }
        }else {
            object.put("msg","版本太低，请先升级版本!");
        }
        return object;
    }

    public JSONObject cancelWithdrawal(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (data != null && data.get("id") != null){
            WithdrawalRecords records = withdrawalRecordsDao.findAllByIdAndUid(Long.parseLong(data.getString("id")),user.getId());
            if (records != null && records.getStatus() == 0){
                object.put("verify",true);
                records.setStatus(-2);
                withdrawalRecordsDao.saveAndFlush(records);
                BalanceOrders orders = new BalanceOrders();
                orders.setUid(user.getId());
                orders.setAddTime(System.currentTimeMillis());
                orders.setUpdateTime(System.currentTimeMillis());
                orders.setAmount(records.getAmount());
                if (records.getReason().contains("余额提现")){
                    int proportionBalance = Integer.parseInt(systemConfigService.getValueByKey("proportionBalance"));
//                    long amount = new Double(records.getAmount() * (1 - (proportionBalance / 100d))).longValue();
                    orders.setAmount(new Double(records.getAmount() / (proportionBalance / 100d)).longValue());
                }
                orders.setStatus(1);
                orders.setReason("提现订单退回: "+records.getOrderNo());
                balanceOrdersDao.saveAndFlush(orders);
            }else {
                object.put("msg","订单不存在或者已进入受理阶段!");
            }
        }
        return object;
    }

    public JSONObject getBalanceRecords(String d, Users user) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        int page = 1;
        if (data != null && data.get("page") != null) page = Integer.parseInt(data.getString("page"));
        page--;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "id"));
        Page<BalanceOrders> balanceOrdersPage = balanceOrdersDao.findAllByUid(user.getId(),pageable);
        for (BalanceOrders record: balanceOrdersPage.getContent()) {
            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(record));
            jsonObject.put("uid","");
            array.add(jsonObject);
        }
        object.put("total",balanceOrdersPage.getTotalPages());
        object.put("list",array);
        return object;
    }
}
