package com.telebott.moviejava.control;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.entity.UploadData;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.UserService;
import com.telebott.moviejava.util.AliOssUtil;
import com.telebott.moviejava.util.MD5Util;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserControl {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthDao authDao;
    @PostMapping("/login")
    public  ResultData login(@RequestBody RequestData requestData){
        ResultData data = new ResultData();
        return data;
    }
    @PostMapping("/bindPhone")
    public ResultData bindPhone(@RequestBody RequestData requestData){
        ResultData data = new ResultData();
//        String salt = RandomStringUtils.randomAlphanumeric(32);
//        MD5Util md5Util = new MD5Util(salt);
//        users.setSalt(salt);
//        users.setPassword(md5Util.getPassWord(md5Util.getMD5(users.getPassword())));
        return data;
    }
    @PostMapping("/info")
    public ResultData getInfo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users users = null;
        if (requestData.getUser() != null){
            users = requestData.getUser();
//            System.out.println(users.getIdentifier());
        }else if (StringUtils.isNotEmpty(requestData.getIdentifier())){
            users = userService.loginByIdentifier(requestData.getIdentifier());
            if (users == null){
                users = authDao.findUserByIdentifier(requestData.getIdentifier());
                if (users == null){
                    Random r = new Random();
                    MD5Util md5Util = new MD5Util();
                    StringBuilder nickname = new StringBuilder("游客_");
                    for (int i = 1; i < 9; i++) {
                        int num = r.nextInt(9); // 生成[0,9]区间的整数
                        nickname.append(num);
                    }
                    users = new Users();
                    users.setIdentifier(requestData.getIdentifier());
                    users.setNickname(nickname.toString());
                    users.setUid(md5Util.getMD5(requestData.getIdentifier()));
//                    users.setAvatar("http://htm-download.oss-cn-hongkong.aliyuncs.com/default_head.gif");
//                userService._save(users);
                    users.setToken(getToken());
                    authDao.pushUser(users);
                }
            }else {
                users.setToken(getToken());
                authDao.pushUser(users);
            }

        }
        data.setData(userService.getResult(users));
        return data;
    }
    @GetMapping("getStsAccount")
    public ResultData getStsAccount(@ModelAttribute UploadData uploadData){
        ResultData data = new ResultData();
        data.setData(JSONObject.parseObject(AliOssUtil.getToken()));
        return data;
    }
    private String getToken(){
//        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
//        HttpSession session = request.getSession();
//        return session.getId().replaceAll("-","");
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }
}
