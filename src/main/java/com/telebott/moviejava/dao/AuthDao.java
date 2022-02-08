package com.telebott.moviejava.dao;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telebott.moviejava.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class AuthDao {
    @Autowired
    UsersDao usersDao;
    @Autowired
    RedisTemplate redisTemplate;
    public void pushCode(String uid, String code){
        JSONObject jsonObject = findCode(uid);
        if (jsonObject != null){
            popCode(jsonObject);
        }
        jsonObject = new JSONObject();
        jsonObject.put("uid", uid);
        jsonObject.put("code", code);
        redisTemplate.opsForSet().add("Code",jsonObject.toJSONString());
    }
    public JSONObject findCode(String id){
        Set users = redisTemplate.opsForSet().members("Code");
        assert users != null;
        JSONObject jsonObject = new JSONObject();
        for (Object user: users) {
            jsonObject = JSONObject.parseObject(user.toString());
            if (id.equals(jsonObject.get("id"))){
                return jsonObject;
            }
        }
        return jsonObject;
    }
    public void popCode(JSONObject code){
        redisTemplate.opsForSet().remove("Code" ,code.toJSONString());
    }
    public void pushUser(Users userToken){
        Set users = redisTemplate.opsForSet().members("Users");
        assert users != null;
//        System.out.println(users.toString());
        for (Object user: users) {
            ObjectMapper objectMapper = new ObjectMapper();
            Users userEntity = objectMapper.convertValue(user, Users.class);
            if (userEntity.getId().equals(userToken.getId()) || userEntity.getToken().equals(userToken.getToken())){
                popUser(userEntity);
            }
        }
        redisTemplate.opsForSet().add("Users",userToken);
    }
    public void updateUser(String token){
        Users user = findUserByToken(token);
        if (user != null){
            user = usersDao.findAllById(user.getId());
            if (user != null){
                user.setToken(token);
                pushUser(user);
            }
        }
    }
    public void popUser(Users userToken){
        redisTemplate.opsForSet().remove("Users" ,userToken);
    }
    public Users findUserByToken(String token) {
        Set users = redisTemplate.opsForSet().members("Users");
        if (users != null){
            for (Object user: users) {
                ObjectMapper objectMapper = new ObjectMapper();
                Users userEntity = objectMapper.convertValue(user,Users.class);
                if (userEntity.getToken().equals(token)){
                    return userEntity;
                }
            }
        }
        return null;
    }
    public Set getAllUser(){
        return redisTemplate.opsForSet().members("Users");
    }
}
