package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.*;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MobileRegularExp;
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
    @Autowired
    private UserService userService;
    @GetMapping("/test")
    public ResultData test(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone("+8618172195974");
        smsRecordsService._sendSmsCode(smsCode);
        data.setMessage(smsCode.getId());
        return data;
    }
    @PostMapping("/checkPhone")
    public ResultData checkPhone(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        if (MobileRegularExp.isMobileNumber(requestData.getData())){
            System.out.println(requestData.getData());
            Users user = userService.getUserByPhone(requestData.getData());
            JSONObject object = new JSONObject();
            if (user != null){
                object.put("ready", true);
            }else {
                object.put("ready", false);
            }
            data.setData(object);
        }else {
            data.setCode(201);
            data.setMessage("手机号码格式不正确!");
        }
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
