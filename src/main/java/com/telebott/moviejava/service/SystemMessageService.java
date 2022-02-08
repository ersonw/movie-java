package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.SystemMessageDao;
import com.telebott.moviejava.entity.SystemMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SystemMessageService {
    @Autowired
    private SystemMessageDao systemMessageDao;
    public void _save(SystemMessage message){
        systemMessageDao.saveAndFlush(message);
    }
    public JSONObject getNewMessage(){
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 1, sort);
        Page<SystemMessage> configPage = systemMessageDao.findAll(pageable);
        JSONObject object = new JSONObject();
        if (configPage.getContent().size() > 0){
            SystemMessage message = configPage.getContent().get(0);
            object.put("id",message.getId());
            object.put("title",message.getTitle());
            object.put("str", message.getStr());
            object.put("date", message.getDate());
        }
        return object;
    }
}
