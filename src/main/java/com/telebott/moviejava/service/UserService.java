package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.util.MD5Util;
import com.telebott.moviejava.util.ToolsUtil;
import com.telebott.moviejava.util.WaLiUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private WaliGameRecordsDao waliGameRecordsDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private VideoRecommendsDao videoRecommendsDao;
    @Autowired
    private UserFollowsDao userFollowsDao;
    @Autowired
    private WaLiGamesDao waLiGamesDao;
    @Autowired
    private BalanceOrdersDao balanceOrdersDao;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private ShareRecordsDao shareRecordsDao;
    @Autowired
    private DiamondRecordsDao diamondRecordsDao;
    @Autowired
    private GoldRecordsDao goldRecordsDao;
    @Autowired
    private GameBalanceOrdersDao gameBalanceOrdersDao;

    public void _save(Users users){
        usersDao.saveAndFlush(users);
    }
    public void _push(Users users){
        authDao.pushUser(users);
    }
    public Users _getInviteOwner(String invite){
        return usersDao.findAllByInvite(invite);
    }
    public void _saveAndPush(Users _user){
        _user.setUtime(System.currentTimeMillis() / 1000);
        if (isUser(_user.getId())){
            _save(_user);
        }
        _push(_user);
    }
    public Users _getById(long id){
        return usersDao.findAllById(id);
    }
    public String _getSalt(){
        return RandomStringUtils.randomAlphanumeric(32);
    }
    public String _getInvite(){
        String invite = RandomStringUtils.randomAlphanumeric(6);
        Users users = usersDao.findAllByInvite(invite);
        if (users != null){
            return _getInvite();
        }
        return invite;
    }
    public Users _change(JSONObject object){
        Users _user = JSONObject.toJavaObject(object,Users.class);
        if (_user != null && isUser(_user.getToken())){
            JSONObject _token = JSONObject.parseObject(JSONObject.toJSONString(authDao.findUserByToken(_user.getToken())));
            Users user = usersDao.findAllByEmail(object.getString("email"));
            if (user != null && user.getId() != _token.getLong("id")){
                return null;
            }
            System.out.println(_user);
            for (Map.Entry<String, Object> entry: object.entrySet()) {
                if (entry.getValue() != null){
                    _token.put(entry.getKey(), entry.getValue());
                }
            }
            _user = JSONObject.toJavaObject(_token,Users.class);
            _saveAndPush(_user);
            return _user;
        }
        return null;
    }
    public boolean isUser(Users users){
        if (users.getId() > 0){
            return isUser(users.getId());
        }else {
            return isUser(users.getToken());
        }
    }
    private boolean isUser(String token){
        return authDao.findUserByToken(token) != null;
    }
    private boolean isUser(long id){
        return usersDao.findAllById(id) != null;
    }
    public Users loginByIdentifier(String identifier){
        return usersDao.findAllByIdentifier(identifier);
    }
    public JSONObject getResult(Users users){
        JSONObject object = new JSONObject();
        if (users != null){
            object.put("nickname",users.getNickname());
            object.put("sex", users.getSex());
            object.put("birthday", users.getBirthday());
            object.put("uid", users.getUid());
            object.put("token", users.getToken());
            object.put("phone", users.getPhone());
            object.put("avatar",users.getAvatar());
            object.put("gold", getGold(users));
            object.put("diamond", getDiamond(users));
            object.put("invite",users.getInvite());
            object.put("superior", users.getSuperior());
            object.put("expired",users.getExpireds());
            object.put("experience",users.getExperience());
            object.put("email",users.getEmail());
            object.put("bkImage",users.getBkImage());
            object.put("remommends",videoRecommendsDao.countAllByUid(users.getId()));
            object.put("follows", userFollowsDao.countAllByUid(users.getId()));
            object.put("fans", userFollowsDao.countAllByToUid(users.getId()));
        }
        return object;
    }
    public Users getUserByPhone(String phone){
        return usersDao.findAllByPhone(phone);
    }
    public JSONObject getBalance(Users user) {
        JSONObject object = new JSONObject();
        long amount = balanceOrdersDao.countAllByUidAndStatus(user.getId(),1);
        if (amount > 0){
            object.put("balance",balanceOrdersDao.countAllByBalance(user.getId()));
        }else {
            object.put("balance",0);
        }
        return object;
    }
    public long getBalance(long uid) {
        long amount = balanceOrdersDao.countAllByUidAndStatus(uid,1);
        if (amount > 0){
            amount = balanceOrdersDao.countAllByBalance(uid);
        }else {
            amount = 0;
        }
        return amount;
    }
    public long getDiamond(Users user) {
        long amount = diamondRecordsDao.countAllByUidAndStatus(user.getId(),1);
        if (amount > 0){
            return diamondRecordsDao.countAllByBalance(user.getId());
        }
        return amount;
    }
    public long getGold(Users user) {
        long amount = goldRecordsDao.countAllByUidAndStatus(user.getId(),1);
        if (amount > 0){
            return goldRecordsDao.countAllByBalance(user.getId());
        }
        return amount;
    }
    public JSONObject getShareCount(Users user) {
        JSONObject object = new JSONObject();
//        object.put("count",usersDao.countAllBySuperior(user.getId()));
        object.put("count",shareRecordsDao.countAllByUid(user.getId()));
        object.put("bgImage", systemConfigService.getValueByKey("shareBgImage"));
        object.put("shareText", systemConfigService.getValueByKey("shareText"));
        object.put("shareUrl", systemConfigService.getValueByKey("shareUrl")+"?code="+user.getInvite());
        return object;
    }

    public Users getUserByUid(String uid) {
        return usersDao.findAllByUid(uid);
    }

    public JSONObject getGameBalance(Users user) {
        JSONObject object = new JSONObject();
        object.put("gameBalance", WaLiUtil.getBalance(user.getId()));
        return object;
    }

    public JSONObject enterGame(String data, Users user) {
        JSONObject objectData = JSONObject.parseObject(data);
        JSONObject object = new JSONObject();
        int gid = 0;
        if (objectData != null && objectData.get("id") != null) gid = objectData.getInteger("id");
        JSONObject gameObject = WaLiUtil.enterGame(user.getId(), gid);
        if (gameObject != null){
            if (gameObject.get("gameReason") != null && gameObject.getString("gameReason").equals("ip_banned")){
                object.put("msg","进入游戏失败，限制地区无法进行游戏!");
            }else{
                if (gid > 0){
                    WaliGameRecords record = new WaliGameRecords();
                    record.setGameId(gid);
                    record.setUid(user.getId());
                    record.setAddTime(System.currentTimeMillis());
                    waliGameRecordsDao.saveAndFlush(record);
                }
                object.put("url", gameObject.getString("gameUrl"));
            }
        }else{
            object.put("msg","进入游戏失败，请联系管理员!");
        }
        return object;
    }
    public  static boolean isNumberString(String s){
        for (int i=0;i< s.length(); i++){
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }
    public JSONObject turnDiamondToGame(Users user, String d) {
        JSONObject object = new JSONObject();
        object.put("verify", false);
        JSONObject data = JSONObject.parseObject(d);
        if (data != null && data.get("num") != null && isNumberString(data.getString("num"))){
            long b = data.getLong("num");
            if (b > 9){
                if (b > getDiamond(user)){
                    object.put("msg", "划转金额比余额大，划转失败!");
                }else{
                    DiamondRecords records = new DiamondRecords();
                    records.setDiamond(-(b));
                    records.setStatus(1);
                    records.setUid(user.getId());
                    records.setReason("划转到游戏余额");
                    records.setAddTime(System.currentTimeMillis());
                    records.setUpdateTime(System.currentTimeMillis());
                    double proportionDiamond = Double.parseDouble(systemConfigService.getValueByKey("proportionDiamond"));
                    long balance = new Double((b / proportionDiamond) * 100d).longValue();
                    GameBalanceOrders order = new GameBalanceOrders();
                    order.setAmount(balance);
                    order.setStatus(1);
                    order.setAddTime(System.currentTimeMillis());
                    order.setUpdateTime(System.currentTimeMillis());
                    order.setUid(user.getId());
                    order.setReason("来自钻石账户的划转");
                    if (WaLiUtil.tranfer(user.getId(), balance)){
                        diamondRecordsDao.saveAndFlush(records);
                        gameBalanceOrdersDao.saveAndFlush(order);
                        object.put("verify", true);
                    }else{
                        object.put("msg", "划转失败,联系管理员!");
                    }
                }
            }else{
                object.put("msg", "划转金额不能小于10钻石，划转失败!");
            }
        }else{
            object.put("msg", "未输入金额，划转失败!");
        }
        return object;
    }
    public JSONObject turnGoldToGame(Users user, String d) {
        JSONObject object = new JSONObject();
        object.put("verify", false);
        JSONObject data = JSONObject.parseObject(d);
        if (data != null && data.get("num") != null && isNumberString(data.getString("num"))){
            long b = data.getLong("num");
            if (b > 9){
                if (b > getGold(user)){
                    object.put("msg", "划转金额比余额大，划转失败!");
                }else{
                    GoldRecords records = new GoldRecords();
                    records.setGold(-(b));
                    records.setStatus(1);
                    records.setUid(user.getId());
                    records.setReason("划转到游戏余额");
                    records.setAddTime(System.currentTimeMillis());
                    records.setUpdateTime(System.currentTimeMillis());
                    double proportionGold = Double.parseDouble(systemConfigService.getValueByKey("proportionGold"));
                    long balance = new Double((b / proportionGold) * 100d).longValue();
                    GameBalanceOrders order = new GameBalanceOrders();
                    order.setAmount(balance);
                    order.setStatus(1);
                    order.setAddTime(System.currentTimeMillis());
                    order.setUpdateTime(System.currentTimeMillis());
                    order.setUid(user.getId());
                    order.setReason("来自金币账户的划转");
                    if (WaLiUtil.tranfer(user.getId(), balance)){
                        goldRecordsDao.saveAndFlush(records);
                        gameBalanceOrdersDao.saveAndFlush(order);
                        object.put("verify", true);
                    }else{
                        object.put("msg", "划转失败,联系管理员!");
                    }
                }
            }else{
                object.put("msg", "划转金额不能小于10金币，划转失败!");
            }
        }else{
            object.put("msg", "未输入金额，划转失败!");
        }
        return object;
    }
    public JSONObject joinChannel(String data, Users users) {
        System.out.println("渠道回参:" + data);
        JSONObject object = new JSONObject();
//        object.put("msg","加入成功！");
        object.put("verify",true);
        return object;
    }

    public JSONObject changeNickname(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null){
            object.put("msg", "过期请求！");
        }else if (users.getNickname().equals(data.getString("change")) || StringUtils.isEmpty(data.getString("change"))){
            object.put("msg", "");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改资料！");
            }else{
                users.setNickname(data.getString("change"));
                users.setUtime(System.currentTimeMillis());
                _saveAndPush(users);
                object.put("verify",true);
            }
        }
        return object;
    }
    public JSONObject changeAvatar(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null){
            object.put("msg", "过期请求！");
        }else if (data.get("change") == null && (StringUtils.isEmpty(data.getString("change")) || users.getAvatar().equals(data.getString("change")))){
            object.put("msg", "");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改头像！");
            }else if (data.getString("change").startsWith("http")){
                users.setAvatar(data.getString("change"));
                users.setUtime(System.currentTimeMillis());
                _saveAndPush(users);
                object.put("verify",true);
            }
        }
        return object;
    }
    public JSONObject changeBgImage(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null){
            object.put("msg", "过期请求！");
        }else if (data.get("change") == null && (StringUtils.isEmpty(data.getString("change")) || users.getAvatar().equals(data.getString("change")))){
            object.put("msg", "");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改背景！");
            }else if (data.getString("change").startsWith("http")){
                users.setBkImage(data.getString("change"));
                users.setUtime(System.currentTimeMillis());
                _saveAndPush(users);
                object.put("verify",true);
            }
        }
        return object;
    }
    public JSONObject changeSex(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null || !isNumberString(data.getString("change"))){
            object.put("msg", "过期请求！");
        }else if (StringUtils.isEmpty(data.getString("change")) || users.getSex() == (data.getInteger("change"))){
            object.put("msg", "");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改资料！");
            }else{
                users.setSex(data.getInteger("change"));
                users.setUtime(System.currentTimeMillis());
                _saveAndPush(users);
                object.put("verify",true);
            }
        }
        return object;
    }
    public JSONObject changeAge(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null || !isNumberString(data.getString("change"))){
            object.put("msg", "过期请求！");
        }else if (StringUtils.isEmpty(data.getString("change")) || users.getBirthday() == (data.getLong("change"))){
            object.put("msg", "");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改资料！");
            }else{
                users.setBirthday(data.getLong("change"));
                users.setUtime(System.currentTimeMillis());
                _saveAndPush(users);
                object.put("verify",true);
            }
        }
        return object;
    }
    public JSONObject changeEmail(String d, Users users) {
//        System.out.println(d);
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null || StringUtils.isEmpty(data.getString("change")) || (StringUtils.isNotEmpty(users.getEmail()) && users.getEmail().equals(data.getString("change")))){
            object.put("msg", "过期请求！");
        }else {
            if (!ToolsUtil.checkEmailFormat(data.getString("change"))){
                object.put("msg", "邮箱格式不正确！");
            }else{
                if (StringUtils.isEmpty(users.getPhone())){
                    object.put("msg", "游客不允许修改邮件！");
                }else{
                    users.setEmail(data.getString("change"));
                    users.setUtime(System.currentTimeMillis());
                    Users _user = usersDao.findAllByEmail(users.getEmail());
                    if (_user != null){
                        object.put("msg", "邮件已绑定其他账号，详情请联系在线客服！");
                    }else{
                        _saveAndPush(users);
                        object.put("verify",true);
                    }
                }
            }
        }
        return object;
    }
    public JSONObject changePassword(String d, Users users) {
        JSONObject data = JSONObject.parseObject(d);
        JSONObject object = new JSONObject();
        object.put("verify",false);
        if (users == null){
            object.put("msg", "登录已过期！");
        }else if (data == null || data.get("change") == null || StringUtils.isEmpty(data.getString("change"))){
            object.put("msg", "过期请求！");
        }else{
            if (StringUtils.isEmpty(users.getPhone())){
                object.put("msg", "游客不允许修改密码！");
            }else{

            }
            MD5Util md5Util = new MD5Util(users.getSalt());
            String pass = md5Util.getPassWord(data.getString("change"));
            String old = StringUtils.isEmpty(data.getString("old")) ? "" : md5Util.getPassWord(data.getString("old"));
            if (StringUtils.isEmpty(old) && StringUtils.isNotEmpty(users.getPassword())){
                object.put("msg", "原密码不能为空！");
            }else {
                users.setUtime(System.currentTimeMillis());
                if (StringUtils.isEmpty(users.getPassword())){
                    users.setPassword(pass);
                    _saveAndPush(users);
                    object.put("verify",true);
                }else{
                    if (users.getPassword().equals(old)){
                        _saveAndPush(users);
                        object.put("verify",true);
                    }else{
                        object.put("msg", "原密码错误！");
                    }
                }
            }
        }
        return object;
    }

    public JSONObject unBindPhone(Users users) {
        JSONObject object = new JSONObject();
        object.put("verify",false);
//        object.put("msg", "活动期间暂不支持解绑手机号，详情请联系在线客服！");
        if (users == null){
            object.put("msg", "用户不存在！");
        }else{
            users.setUtime(System.currentTimeMillis());
            users.setPhone("");
            _saveAndPush(users);
            object.put("verify",true);
        }
        return object;
    }
}
