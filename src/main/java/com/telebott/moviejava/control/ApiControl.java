package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.MoblieConfigService;
import com.telebott.moviejava.service.SmsBaoService;
import com.telebott.moviejava.service.SmsRecordsService;
import com.telebott.moviejava.service.SystemMessageService;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.SmsBaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiControl {
    @Autowired
    private MoblieConfigService configService;
    @Autowired
    private SystemMessageService messageService;
    @Autowired
    private SmsRecordsService smsRecordsService;
    @GetMapping("/test")
    public ResultData test(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone("+8618172195974");
        smsRecordsService._sendSmsCode(smsCode);
        data.setMessage(smsCode.getId());
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
