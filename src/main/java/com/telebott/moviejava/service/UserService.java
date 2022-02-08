package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.UsersDao;
import com.telebott.moviejava.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    public void _save(Users users){
        usersDao.saveAndFlush(users);
    }
    public Users loginByIdentifier(String identifier){
        return usersDao.findAllByIdentifier(identifier);
    }
    public JSONObject getResult(Users users){
        JSONObject object = new JSONObject();
        if (users != null){
            object.put("nickname",users.getNickname());
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
