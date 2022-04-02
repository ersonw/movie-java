package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.VideoActorsDao;
import com.telebott.moviejava.entity.*;
import com.telebott.moviejava.service.*;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MD5Util;
import com.telebott.moviejava.util.MobileRegularExp;
import com.telebott.moviejava.util.SmsBaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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
    @Autowired
    private VideosService videosService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    private VideoFeaturedsService videoFeaturedsService;
    @Autowired
    private OnlineOrderService onlineOrderService;
    @GetMapping("/test")
    public ResultData test(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        SmsCode smsCode = new SmsCode();
        smsCode.setPhone("+8618172195974");
        smsRecordsService._sendSmsCode(smsCode);
        data.setMessage(smsCode.getId());
        return data;
    }
    @GetMapping("/carousels")
    public ResultData carousels(){
        ResultData data = new ResultData();
        data.setData(systemConfigService.getCarousel());
        return data;
    }
    @GetMapping("/featureds")
    public ResultData featureds(){
        ResultData data = new ResultData();
        data.setData(videoFeaturedsService.getFeatureds());
        return data;
    }
    @GetMapping("/featuredTags")
    public ResultData featuredTags(){
        ResultData data = new ResultData();
        data.setData(videoFeaturedsService.getFeaturedTags());
        return data;
    }
    @GetMapping("/measurementTags")
    public ResultData measurementTags(){
        ResultData data = new ResultData();
        data.setData(videoFeaturedsService.getMeasurementTags());
        return data;
    }
    @GetMapping("/ActorLists")
    public ResultData ActorLists(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videoFeaturedsService.getActorLists(requestData.getData()));
        return data;
    }
    @GetMapping("/Actor")
    public ResultData Actor(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.Actor(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/Recommends")
    public ResultData Recommends(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.Recommends(requestData.getData(),requestData.getUser()));
        return data;
    }
    @GetMapping("/ActorVideos")
    public ResultData ActorVideos(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.ActorVideos(requestData.getData()));
        return data;
    }
    @GetMapping("/featuredLists")
    public ResultData featuredLists(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videoFeaturedsService.getFeaturedLists(requestData.getData()));
        return data;
    }
    @GetMapping("/PopularList")
    public ResultData PopularList(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.getPopularList(requestData.getData()));
        return data;
    }
    @GetMapping("/classLists")
    public ResultData classLists(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.classLists());
        return data;
    }
    @GetMapping("/classTags")
    public ResultData classTags(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.classTags());
        return data;
    }
    @GetMapping("/classVideos")
    public ResultData classVideos(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        data.setData(videosService.classVideos(requestData.getData()));
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
            System.out.println(phone);
//            System.out.println(requestData.getData());
            if (phone != null){
                user.setUtime(System.currentTimeMillis());
                user.setPhone(phone);
                user.setIdentifier(object.get("identifier").toString());
                user.setUid(md5Util.getMD5(user.getIdentifier()));
                user.setSalt(userService._getSalt());
                user.setInvite(userService._getInvite());
                md5Util.setSalt(user.getSalt());
                if (object.get("passwd").toString().length() < 32){
                    user.setPassword(md5Util.getPassWord(md5Util.getMD5(object.get("passwd").toString())));
                }else {
                    user.setPassword(md5Util.getPassWord(object.get("passwd").toString()));
                }
                userService._save(user);
                userService._push(user);
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
                    data.setMessage("短信发送失败，请检查手机号码是否正确!");
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
//    @RequestMapping("/Yzm")
//    public  ResultData Yzm(HttpServletRequest httpServletRequest){
//        ResultData data = new ResultData();
//        String jsonStr = getJsonBodyString(httpServletRequest);
//        if (jsonStr != null){
//            JSONObject object = JSONObject.parseObject(jsonStr);
//            if (object != null){
//                YzmData yzmData = JSONObject.toJavaObject(object,YzmData.class);
//                if (yzmData != null && yzmData.getResult().equals("ok")){
//                    videosService.handlerYzm(yzmData);
//                }
//            }
//        }
//        return data;
//    }
//    private String getJsonBodyString(HttpServletRequest httpServletRequest){
//        try {
//            httpServletRequest.setCharacterEncoding("UTF-8");
//            StringBuilder buffer = new StringBuilder();
//            BufferedReader reader=null;
//            reader = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream(), StandardCharsets.UTF_8));
//            String line=null;
//            while((line = reader.readLine())!=null){
//                buffer.append(line);
//            }
////            System.out.println(buffer);
//            return buffer.toString();
////            InputStream inputStream = httpServletRequest.getInputStream();
////            StringBuilder stringBuilder = new StringBuilder();
////            int temp;
////            while ((temp = inputStream.read()) != -1)
////            {
////                stringBuilder.append((char) temp);
////            }
////            return stringBuilder.toString();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//    @PostMapping("/toPayNotify")
//    public String toPayNotify(@ModelAttribute ToPayNotify payNotify){
//        if(onlineOrderService.handlerToPayNotify(payNotify)){
//            return "success";
//        }
//        return "fail";
//    }
//    @RequestMapping("/toPay")
//    public String toPay(@ModelAttribute ToPayNotify payNotify){
//        return "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<head>\n" +
//                "    <meta charset=\"utf-8\">\n" +
//                "</head>\n" +
//                "<script type=\"text/javascript\">\n" +
//                "\n" +
//                "    function run(){\n" +
//                "        document.getElementById(\"sp\").click();\n" +
//                "    }\n" +
//                "</script>\n" +
//                "<body οnlοad=\"run()\">\n" +
//                "<a href=\"moviescheme://123\">打开应用<h1 id=\"sp\"></h1></a>\n" +
//                "</body>\n" +
//                "</html>";
//    }
}
