package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.*;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MD5Util;
import com.telebott.moviejava.util.TimeUtil;
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
    private WaLiService waLiService;
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
    @Autowired
    private OnlinePayService onlinePayService;
    @Autowired
    private SystemConfigService systemConfigService;
    @GetMapping("/collectActor")
    public ResultData collectActor(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.collectActor(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/collectList")
    public ResultData collectList(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.collectList(requestData.getData(),requestData.getUser()));
        return data;
    }
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
    @GetMapping("/crateGameOrder")
    public ResultData crateGameOrder(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("id") == null  ){
            data.setCode(201);
            data.setMessage("版本太低，请先升级版本!");
        }else {
            data.setData(onlinePayService._crateOrder(user,object.get("id").toString()));
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
    @GetMapping("/turnDiamondToGame")
    public ResultData turnDiamondToGame(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(userService.turnDiamondToGame(user,requestData.getData()));
        return data;
    }
    @GetMapping("/turnGoldToGame")
    public ResultData turnGoldToGame(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users user = requestData.getUser();
        data.setData(userService.turnGoldToGame(user,requestData.getData()));
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

    @GetMapping("/getWithdrawal")
    public ResultData getWithdrawal(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.getWithdrawal(requestData.getUser()));
        return data;
    }
    @GetMapping("/getWithdrawalRecords")
    public ResultData getWithdrawalRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.getWithdrawalRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/Withdrawal")
    public ResultData Withdrawal(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.Withdrawal(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/addCard")
    public ResultData addCard(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.addCard(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/cancelWithdrawal")
    public ResultData cancelWithdrawal(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.cancelWithdrawal(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/getBalanceRecords")
    public ResultData getBalanceRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlineOrderService.getBalanceRecords(requestData.getData(),requestData.getUser()));
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
    @GetMapping("/getCashInOrders")
    public ResultData getCashInOrders(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(onlinePayService.getCashInOrders(requestData.getData(),requestData.getUser()));
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
                user.setUtime(System.currentTimeMillis());
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
            if (users.getId() > 0){
                Users _user = userService._getById(users.getId());
                _user.setToken(users.getToken());
                authDao.pushUser(_user);
            }
//            System.out.println(users.getIdentifier());
        }else if (StringUtils.isNotEmpty(requestData.getIdentifier())){
            users = userService.loginByIdentifier(requestData.getIdentifier());
            MD5Util md5Util = new MD5Util();
            String uid = md5Util.getMD5(requestData.getIdentifier());
            if (users == null){
                users = authDao.findUserByIdentifier(requestData.getIdentifier());
                if (users == null){
                    users = userService.getUserByUid(uid);
                    if (users == null){
                        Random r = new Random();
                        StringBuilder nickname = new StringBuilder("游客_");
                        for (int i = 1; i < 9; i++) {
                            int num = r.nextInt(9); // 生成[0,9]区间的整数
                            nickname.append(num);
                        }
                        users = new Users();
                        users.setIdentifier(requestData.getIdentifier());
                        users.setNickname(nickname.toString());
                        users.setUid(uid);
                        users.setCtime(System.currentTimeMillis() );
                        users.setUtime(System.currentTimeMillis());
                        users.setStatus(1);
                        boolean registerGift = false;
                        String str = systemConfigService.getValueByKey("registerGift");
                        if (StringUtils.isNotEmpty(str) && UserService.isNumberString(str)){
                            if (str.equals(1)){
                                registerGift = true;
                            }
                        }
                        if (registerGift) {
                            users.setExpireds(TimeUtil.manyDaysLater(1));
                        }
//                    users.setAvatar("http://htm-download.oss-cn-hongkong.aliyuncs.com/default_head.gif");
                        userService._save(users);
                        users.setToken(getToken());
                        authDao.pushUser(users);
                    }else{
                        users.setToken(getToken());
                        authDao.pushUser(users);
                    }
                }
            }else {
                users.setToken(getToken());
                authDao.pushUser(users);
            }

        }
        if(users.getStatus() == 1){
            data.setData(userService.getResult(users));
        }else{
            data.setCode(106);
            data.setMessage("账号状态异常！");
        }
        return data;
    }
    @GetMapping("/getStsAccount")
    public ResultData getStsAccount(@ModelAttribute UploadData uploadData){
        ResultData data = new ResultData();
        data.setData(JSONObject.parseObject(AliOssUtil.getToken()));
//        System.out.println(data);
        return data;
    }
    @GetMapping("/getUserInfo")
    public ResultData getUserInfo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.getUserInfo(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/followUser")
    public ResultData followUser(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.followUser(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/PushRecords")
    public ResultData PushRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.PushRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/likeUserVideo")
    public ResultData likeUserVideo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.likeUserVideo(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/VideoRecords")
    public ResultData VideoRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.VideoRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/RecommendRecords")
    public ResultData RecommendRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.RecommendRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/followRecords")
    public ResultData followRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.followRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/fansRecords")
    public ResultData fansRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.fansRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/getBalance")
    public ResultData getBalance(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.getBalance(requestData.getUser()));
        return data;
    }
    @GetMapping("/getShareCount")
    public ResultData getShareCount(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.getShareCount(requestData.getUser()));
        return data;
    }
    @GetMapping("/shareRecords")
    public ResultData shareRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.shareRecords(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/getGameBalance")
    public ResultData getGameBalance(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.getGameBalance(requestData.getUser()));
        return data;
    }
    @GetMapping("/enterGame")
    public ResultData enterGame(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.enterGame(requestData.getData(),requestData.getUser()));
        return data;
    }
//    @GetMapping("/joinChannel")
//    public ResultData joinChannel(@ModelAttribute RequestData requestData){
//        ResultData data = new ResultData();
//        data.setData(userService.joinChannel(requestData.getData(), requestData.getUser()));
//        return data;
//    }
    @PostMapping("/joinChannel")
    public ResultData joinChannel(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.joinChannel(requestData.getData(),requestData.getUser()));
        return data;
    }
    @PostMapping("/joinInvite")
    public ResultData joinInvite(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.joinInvite(requestData.getData(), requestData.getUser()));
        return data;
    }
    @PostMapping("/unBindPhone")
    public ResultData unBindPhone(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.unBindPhone(requestData.getUser()));
        return data;
    }
    @PostMapping("/changeNickname")
    public ResultData changeNickname(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeNickname(requestData.getData(), requestData.getUser()));
        return data;
    }
    @PostMapping("/changeAvatar")
    public ResultData changeAvatar(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeAvatar(requestData.getData(),requestData.getUser()));
        return data;
    }
    @PostMapping("/changeBgImage")
    public ResultData changeBgImage(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeBgImage(requestData.getData(),requestData.getUser()));
        return data;
    }
    @PostMapping("/changeEmail")
    public ResultData changeEmail(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeEmail(requestData.getData(), requestData.getUser()));
        return data;
    }
    @PostMapping("/changePassword")
    public ResultData changePassword(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changePassword(requestData.getData(), requestData.getUser()));
        return data;
    }
    @PostMapping("/changeSex")
    public ResultData changeSex(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeSex(requestData.getData(), requestData.getUser()));
        return data;
    }
    @PostMapping("/changeAge")
    public ResultData changeAge(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(userService.changeAge(requestData.getData(), requestData.getUser()));
        return data;
    }

    @GetMapping("/getRecords")
    public ResultData getRecords(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(waLiService.getRecords(requestData.getUser()));
        return data;
    }
//    @PostMapping("/joinInvite")
//    public ResultData joinInvite(@ModelAttribute RequestData requestData, @RequestAttribute String user){
//        ResultData data = new ResultData();
////        System.out.println(user);
//        Users users =  JSONObject.toJavaObject(JSONObject.parseObject(user),Users.class);
//        data.setData(videosService.joinInvite(requestData.getData(),users));
//        return data;
//    }
    private String getToken(){
//        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        HttpSession session = request.getSession();
//        return session.getId().replaceAll("-","");
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }
}
