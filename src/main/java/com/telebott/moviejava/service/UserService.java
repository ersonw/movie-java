package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.UsersDao;
import com.telebott.moviejava.entity.Users;
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
    public void _save(Users users){
        usersDao.saveAndFlush(users);
    }
    public Users _change(JSONObject object){
        Users _user = JSONObject.toJavaObject(object,Users.class);
        if (_user != null && isUser(_user.getToken())){
            JSONObject _token = JSONObject.parseObject(JSONObject.toJSONString(authDao.findUserByToken(_user.getToken())));
            for (Map.Entry<String, Object> entry: object.entrySet()) {
                if (entry.getValue() != null){
                    _token.put(entry.getKey(), entry.getValue());
                }
            }
            _user = JSONObject.toJavaObject(_token,Users.class);
            if (isUser(_user.getId())){
                _save(_user);
            }
            _user.setUtime(System.currentTimeMillis() / 1000);
            authDao.pushUser(_user);
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
            object.put("birthday", users.getBrithday());
            object.put("uid", users.getUid());
            object.put("token", users.getToken());
            object.put("phone", users.getPhone());
            object.put("avatar",users.getAvatar());
            object.put("gold",users.getGold());
            object.put("diamond", users.getDiamond());
            object.put("invite",users.getInvite());
        }
        return object;
    }

}
