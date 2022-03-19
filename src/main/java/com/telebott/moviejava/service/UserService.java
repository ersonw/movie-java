package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.BalanceOrders;
import com.telebott.moviejava.entity.UserFollows;
import com.telebott.moviejava.entity.Users;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private VideoRecommendsDao videoRecommendsDao;
    @Autowired
    private UserFollowsDao userFollowsDao;
    @Autowired
    private UserPostsDao userPostsDao;
    @Autowired
    private BalanceOrdersDao balanceOrdersDao;
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
            for (Map.Entry<String, Object> entry: object.entrySet()) {
//                if (StringUtils.isNotEmpty(entry.getValue().toString())){
                    _token.put(entry.getKey(), entry.getValue());
//                }
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
            object.put("gold",users.getGold());
            object.put("diamond", users.getDiamond());
            object.put("invite",users.getInvite());
            object.put("superior", users.getSuperior());
            object.put("expired",users.getExpireds());
            object.put("experience",users.getExperience());
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
        long amount = balanceOrdersDao.countAllByUid(user.getId());
        if (amount > 0){
            object.put("balance",balanceOrdersDao.countAllByBalance(user.getId()));
        }else {
            object.put("balance",0);
        }
        return object;
    }
}
