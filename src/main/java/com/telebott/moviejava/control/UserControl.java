package com.telebott.moviejava.control;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.UserService;
import com.telebott.moviejava.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

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
        return data;
    }
    @PostMapping("/info")
    public ResultData getInfo(@ModelAttribute RequestData requestData){
        ResultData data = new ResultData();
        Users users = null;
        if (requestData.getUser() != null){
            users = requestData.getUser();
        }else if (StringUtils.isNotEmpty(requestData.getIdentifier())){
            users = userService.loginByIdentifier(requestData.getIdentifier());
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
                users.setAvatar("http://htm-download.oss-cn-hongkong.aliyuncs.com/default_head.gif");
//                userService._save(users);
                users.setToken(getToken());
                authDao.pushUser(users);
            }
        }
        data.setData(userService.getResult(users));
        return data;
    }
    private String getToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        return session.getId().replaceAll("-","");
    }
}
