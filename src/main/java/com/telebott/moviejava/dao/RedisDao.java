package com.telebott.moviejava.dao;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.RedisUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class RedisDao {
    private static final String uploadKey = "upload";
    @Autowired
    RedisTemplate redisTemplate;
    public void uploadFile(RedisUpload redisUpload){
        if (existUpload(redisUpload)){
            deleteUpload(redisUpload.getId());
        }
        redisTemplate.opsForSet().add(uploadKey,JSONObject.toJSONString(redisUpload));
    }
    public RedisUpload getById(String id){
        Set members = redisTemplate.opsForSet().members(uploadKey);
        if (members != null){
            for (Object member : members) {
                JSONObject object = JSONObject.parseObject(member.toString());
                if (object != null){
                    if (id.equals(object.get("id").toString())){
                        return JSONObject.toJavaObject(object,RedisUpload.class);
                    }
                }
            }
        }
        return null;
    }
    public boolean existUpload(RedisUpload redisUpload){
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(uploadKey, JSONObject.toJSONString(redisUpload)))){
            return true;
        }
        Set members = redisTemplate.opsForSet().members(uploadKey);
        if (members != null){
            for (Object member : members) {
                JSONObject object = JSONObject.parseObject(member.toString());
                if (object != null){
                    if (redisUpload.getId().equals(object.get("id").toString())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    public void deleteUpload(String id){
        Set members = redisTemplate.opsForSet().members(uploadKey);
        if (members != null){
            for (Object member : members) {
                JSONObject object = JSONObject.parseObject(member.toString());
                if (object != null){
                    if (id.equals(object.get("id").toString())){
                        redisTemplate.opsForSet().remove(uploadKey, object);
                    }
                }
            }
        }
    }
}
