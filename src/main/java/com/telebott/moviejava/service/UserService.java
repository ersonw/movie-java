package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.UsersDao;
import com.telebott.moviejava.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    public void _save(Users users){
        usersDao.save(users);
    }
    public Users loginByIdentifier(String identifier){

    }
    public JSONObject getResult(Users users){
        JSONObject object = new JSONObject();
        if (users != null){
            object.put("nickname",users.getNickname());
            object.put("uid", users.getUid);

        }
        return object;
    }
}
