package com.telebott.moviejava.control;

import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.ResultData;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/user")
public class UserControl {
    @Autowired
    private UserService userService;
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
        }
//        System.out.println(getToken());
        if (users != null){
            users.setToken(getToken());
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
