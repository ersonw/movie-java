package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.*;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MD5Util;
import com.telebott.moviejava.util.MobileRegularExp;
import com.telebott.moviejava.util.SmsBaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;
import java.util.UUID;

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
    @GetMapping("/forgotPasswd")
    public ResultData forgotPasswd(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (object.get("code") == null ||
                object.get("phone") == null ||
                object.get("id") == null ||
                object.get("passwd") == null
        ){
            data.setCode(201);
            data.setMessage("参数提交错误！请更新至最新版本！");
        }else {
            Users user = userService.getUserByPhone(object.get("phone").toString());
            if (user == null){
                data.setCode(202);
                data.setMessage("手机号尚未注册，请先注册手机号！");
            }else {
                String result = smsRecordsService._verifyCode(object.get("id").toString(),object.get("code").toString());
                System.out.println(result);
                if (result != null){
                    MD5Util md5Util = new MD5Util(user.getSalt());
                    if (object.get("passwd").toString().length() < 32){
                        user.setPassword(md5Util.getPassWord(md5Util.getMD5(object.get("passwd").toString())));
                    }else {
                        user.setPassword(md5Util.getPassWord(object.get("passwd").toString()));
                    }
                    userService._saveAndPush(user);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("verify", true);
                    data.setData(jsonObject);
                }else {
                    data.setCode(202);
                    data.setMessage("验证码已过期，请重新获取！");
                }
            }
        }
        return data;
    }
    @GetMapping("/login")
    public ResultData login(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        Users user = requestData.getUser();
        if (object.get("identifier") == null ||
                object.get("phone") == null ||
                object.get("passwd") == null
        ){
            data.setCode(201);
            data.setMessage("参数提交错误！请更新至最新版本！");
        }else {
            Users userNew = userService.getUserByPhone(object.get("phone").toString());
            if (userNew == null){
                data.setCode(202);
                data.setMessage("手机号未注册！");
            }else {
                MD5Util md5Util = new MD5Util(userNew.getSalt());
                String verifyPass = md5Util.getPassWord(object.get("passwd").toString());
                if (verifyPass.equals(userNew.getPassword())){
                    if (user != null &&  userNew.getId() != user.getId()){
                        user.setIdentifier("");
                        userService._saveAndPush(user);
                    }
                    userNew.setIdentifier(object.get("identifier").toString());
                    userService._save(userNew);
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("verify", true);
                    data.setData(jsonObject);
                }else {
                    data.setCode(203);
                    data.setMessage("登录密码错误！");
                }
            }
        }
        return data;
    }
    @GetMapping("/register")
    public ResultData register(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Random r = new Random();
        MD5Util md5Util = new MD5Util();
        Users user = requestData.getUser();
        JSONObject object = JSONObject.parseObject(requestData.getData());
        if (user == null){
            StringBuilder nickname = new StringBuilder("游客_");
            for (int i = 1; i < 9; i++) {
                int num = r.nextInt(9); // 生成[0,9]区间的整数
                nickname.append(num);
            }
            user = new Users();
            user.setNickname(nickname.toString());
            user.setCtime(System.currentTimeMillis() / 1000L);
        }
        if (object.get("id") == null ||
                object.get("identifier") == null ||
                object.get("code") == null ||
                object.get("passwd") == null
        ){
            data.setCode(201);
            data.setMessage("参数提交错误！请更新至最新版本！");
        }else {
            String phone = smsRecordsService._verifyCode(object.get("id").toString(),object.get("code").toString());
            if (phone != null){
                user.setUtime(System.currentTimeMillis() / 1000L);
                user.setPhone(phone);
                user.setIdentifier(object.get("identifier").toString());
                user.setUid(md5Util.getMD5(user.getIdentifier()));
                user.setSalt(userService._getSalt());
                md5Util.setSalt(user.getSalt());
                if (object.get("passwd").toString().length() < 32){
                    user.setPassword(md5Util.getPassWord(md5Util.getMD5(object.get("passwd").toString())));
                }else {
                    user.setPassword(md5Util.getPassWord(object.get("passwd").toString()));
                }
                userService._save(user);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("verify", true);
                data.setData(jsonObject);
            }else {
                data.setCode(201);
                data.setMessage("验证码不正确或已过期！");
            }

        }
        return data;
    }
    @GetMapping("/sendSms")
    public ResultData sendSms(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        if (MobileRegularExp.isMobileNumber(requestData.getData())){
            if (smsRecordsService._checkSmsMax(requestData.getData())){
                SmsCode smsCode = new SmsCode();
                smsCode.setPhone(requestData.getData());
                if (smsRecordsService._sendSmsCode(smsCode)){
                    JSONObject object = new JSONObject();
                    object.put("id", smsCode.getId());
                    data.setData(object);
                }else {
                    data.setCode(202);
                    data.setMessage("短信发送失败，请联系管理员!");
                }
            }else {
                data.setCode(203);
                data.setMessage("短信发送失败，今日已达到发送上限!");
            }
        }else {
            data.setCode(201);
            data.setMessage("手机号码格式不正确!");
        }
        return data;
    }
    @GetMapping("/checkPhone")
    public ResultData checkPhone(@ModelAttribute RequestData requestData){
//        System.out.println(requestData);
        ResultData data = new ResultData();
//        return data;
        if (MobileRegularExp.isMobileNumber(requestData.getData())){
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
    private String getToken(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }

}
