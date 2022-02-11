package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.entity.UploadData;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.MoblieConfigService;
import com.telebott.moviejava.service.SystemMessageService;
import com.telebott.moviejava.util.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControl {
    @Autowired
    private MoblieConfigService configService;
    @Autowired
    private SystemMessageService messageService;
    @GetMapping("/test")
    public ResultData test(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(JSONObject.parseObject(AliOssUtil.getToken()));
        return data;
    }
    @GetMapping("/getConfig")
    public ResultData getConfig(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(configService.getConfig());
        return data;
    }
    @GetMapping("/checkVersion")
    public ResultData checkVersion(){
        ResultData data = new ResultData();
        data.setData(configService.checkVersion());
        return data;
    }
    @GetMapping("/getSystemMessage")
    public ResultData getSystemMessage(@ModelAttribute RequestData requestData){
//        Users users = requestData.getUser();
//        System.out.println(users.getIdentifier());
        ResultData data = new ResultData();
        data.setData(messageService.getNewMessage());
        return data;
    }
    @PostMapping("uploadImage")
    public ResultData uploadImage(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        return data;
    }
    @PostMapping("uploadFile")
    public ResultData uploadFile(@ModelAttribute UploadData uploadData){
        ResultData data = new ResultData();
        return data;
    }

}
