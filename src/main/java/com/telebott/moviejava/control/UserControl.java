package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.*;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserControl {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private SmsRecordsService smsRecordsService;
    @Autowired
    private OnlineOrderService onlineOrderService;
    @Autowired
    private CommodityVipOrderService commodityVipOrderService;
    @Autowired
    private CommodityDiamondOrderService commodityDiamondOrderService;
    @Autowired
    private CommodityGoldOrderService commodityGoldOrderService;
    @Autowired
    private VideosService videosService;
    @GetMapping("/cancelVipOrder")
    public ResultData cancelOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityVipOrderService._cancelOrder(user, object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/crateVipOrder")
    public ResultData crateVipOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null  ){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityVipOrderService._crateOrder(user,object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/getVipOrder")
    public ResultData getVipOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(commodityVipOrderService._getOrder(user,requestData.getData()));
        return data;
    }
    @GetMapping("/cancelDiamondOrder")
    public ResultData cancelDiamondOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityDiamondOrderService._cancelOrder(user, object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/crateDiamondOrder")
    public ResultData crateDiamondOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null  ){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityDiamondOrderService._crateOrder(user,object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/getDiamondOrder")
    public ResultData getDiamondOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(commodityDiamondOrderService._getOrder(user,requestData.getData()));
        return data;
    }
    @GetMapping("/getDiamondRecords")
    public ResultData getDiamondRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(commodityDiamondOrderService._getRecords(user,requestData.getData()));
//        System.out.println(data);
        return data;
    }
    @GetMapping("/cancelGoldOrder")
    public ResultData cancelGoldOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityGoldOrderService._cancelOrder(user, object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/crateGoldOrder")
    public ResultData crateGoldOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null  ){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(commodityGoldOrderService._crateOrder(user,object.get("id").toString()));
        }
        return data;
    }
    @GetMapping("/getGoldOrder")
    public ResultData getGoldOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(commodityGoldOrderService._getOrder(user,requestData.getData()));
        return data;
    }
    @GetMapping("/getGoldRecords")
    public ResultData getGoldRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(commodityGoldOrderService._getRecords(user,requestData.getData()));
//        System.out.println(data);
        return data;
    }

    @GetMapping("/postCrateOrder")
    public ResultData postCrateOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("order_id") == null ||
                object.get("pid") == null ||
                object.get("type") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(onlineOrderService._postCrateOrder(user,object.get("type").toString(),object.get("order_id").toString(),object.get("pid").toString()));
        }
        return data;
    }
    @GetMapping("/getOrder")
    public ResultData getOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("order_id") == null ||
                object.get("type") == null ){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(onlineOrderService._getOrder(object.get("type").toString(),object.get("order_id").toString()));
        }
        return data;
    }
    @GetMapping("/getOrders")
    public ResultData getOrders(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        data = onlineOrderService._getOrders(user, data, object.get("page").toString());
        return data;
    }
    @GetMapping("/changePhone")
    public  ResultData changePhone(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null ||
                object.get("code") == null||
                object.get("phone") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else if (user.getPhone().equals(object.get("phone").toString())){
            data.setCode(202);
            data.setMessage("原手机号与新手机号相同!");
        }else {
            String phone = smsRecordsService._verifyCode(object.get("id").toString(),object.get("code").toString());
            if (phone == null){
                data.setCode(203);
                data.setMessage("验证码不存在或者已过期!");
            }else {
                user.setPhone(object.get("phone").toString());
                userService._saveAndPush(user);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verify", true);
                data.setData(jsonObject);
            }
        }
        return data;
    }
    @GetMapping("/changePhoneCheck")
    public  ResultData changePhoneCheck(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        Users user = requestData.getUser();
        if (object.get("phoneOld") == null || object.get("phone") == null){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else if (Objects.equals(object.get("phoneOld").toString(), object.get("phone").toString())){
            data.setCode(202);
            data.setMessage("原手机号与新手机号相同!");
        }else if (!Objects.equals(object.get("phoneOld").toString(), user.getPhone())){
            data.setCode(203);
            data.setMessage("原手机号输入有误!");
        }else {
            System.out.println(object.get("phoneOld"));
            System.out.println(object.get("phone"));
            Users phone = userService.getUserByPhone(object.get("phone").toString());
            if (phone != null){
                data.setCode(204);
                data.setMessage("新手机号已经绑定其他设备，请先从其他设备上解绑!");
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verify", true);
                data.setData(jsonObject);
            }
        }
        return data;
    }
    @GetMapping("/checkInviteCode")
    public  ResultData checkInviteCode(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        Users user = requestData.getUser();
        if (user.getId() == 0 || StringUtils.isEmpty(user.getPhone())){
            data.setCode(201);
            data.setMessage("用户未绑定手机号!");
        }else if (user.getSuperior() > 0){
            data.setCode(202);
            data.setMessage("此用户领取过!");
        }else if (object.get("code") == null){
            data.setCode(203);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            Users userOwner = userService._getInviteOwner(object.get("code").toString());
            if (userOwner == null){
                data.setCode(204);
                data.setMessage("礼包码不正确!");
            }else if (userOwner.getId() == user.getId()){
                data.setCode(205);
                data.setMessage("不能兑换自己的礼包码!");
            }else {
                user.setSuperior(userOwner.getId());
                userService._saveAndPush(user);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verify", true);
                data.setData(jsonObject);
            }
        }
        return data;
    }
    @PostMapping("/info")
    public ResultData getInfo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users users = null;
        if (requestData.getUser() != null){
            users = requestData.getUser();
//            System.out.println(users.getIdentifier());
        }else if (StringUtils.isNotEmpty(requestData.getIdentifier())){
            users = userService.loginByIdentifier(requestData.getIdentifier());
            if (users == null){
                users = authDao.findUserByIdentifier(requestData.getIdentifier());
                if (users == null){
                    Random r = new Random();
                    MD5Util md5Util = new MD5Util();
                    StringBuilder nickname = new StringBuilder("游客_");
                    for (int i = 1; i < 9; i++) {
                        int num = r.nextInt(9); // 生成[0,9]区间的整数
                        nickname.append(num);
                    }
                    users = new Users();
                    users.setIdentifier(requestData.getIdentifier());
                    users.setNickname(nickname.toString());
                    users.setUid(md5Util.getMD5(requestData.getIdentifier()));
//                    users.setAvatar("http://htm-download.oss-cn-hongkong.aliyuncs.com/default_head.gif");
//                userService._save(users);
                    users.setToken(getToken());
                    authDao.pushUser(users);
                }
            }else {
                users.setToken(getToken());
                authDao.pushUser(users);
            }

        }
        data.setData(userService.getResult(users));
        return data;
    }
    @GetMapping("getStsAccount")
    public ResultData getStsAccount(@ModelAttribute UploadData uploadData){
        ResultData data = new ResultData();
        data.setData(JSONObject.parseObject(AliOssUtil.getToken()));
        return data;
    }
    private String getToken(){
//        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        HttpSession session = request.getSession();
//        return session.getId().replaceAll("-","");
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }
}
