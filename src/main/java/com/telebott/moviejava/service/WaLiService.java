package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.*;
import com.telebott.moviejava.entity.Marquee;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.entity.WaLiGames;
import com.telebott.moviejava.entity.WaliGameRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WaLiService {
    @Autowired
    private WaLiConfigDao waLiConfigDao;
    @Autowired
    private WaLiGamesDao waLiGamesDao;
    @Autowired
    private WaliGameRecordsDao waliGameRecordsDao;
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private MarqueeDao  marqueeDao;

    public JSONObject getGames() {
        JSONObject object = new JSONObject();
        List<WaLiGames> waLiGames = waLiGamesDao.findAllByStatus(1);
        JSONArray array = new JSONArray();
        for (WaLiGames game : waLiGames) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", game.getId());
            jsonObject.put("name", game.getName());
            jsonObject.put("image", game.getImage());
            array.add(jsonObject);
        }
        object.put("list", array);
        return object;
    }

    public JSONObject getRecords(Users user) {
        JSONObject object = new JSONObject();
        JSONArray array = new JSONArray();
        List<WaliGameRecords> records = waliGameRecordsDao.getRecords(user.getId());
        for (WaliGameRecords record: records) {
            if (record != null){
                WaLiGames game = waLiGamesDao.findAllById(record.getGameId());
                if (game != null){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", game.getId());
                    jsonObject.put("name", game.getName());
                    jsonObject.put("image", game.getImage());
                    array.add(jsonObject);
                }
            }
        }
        object.put("list",array);
        return object;
    }
    //获取 Marquee 所有字段
    public JSONObject getTrumpets() {
        JSONObject object = new JSONObject();
        List<Marquee> marqueeList = marqueeDao.findAll();
        JSONArray array = new JSONArray();
        for (Marquee marquee : marqueeList) {
            JSONObject json = new JSONObject();
            json.put("id",marquee.getId());
            json.put("text",marquee.getText());
            array.add(json);
        }
        object.put("list",array);
        return object;
    }
}
