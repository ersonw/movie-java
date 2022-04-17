package com.telebott.moviejava.service;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.RequestData;
import com.telebott.moviejava.entity.Users;
//import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final AuthDao authDao;
    public AuthInterceptor(AuthDao authDao) {
        this.authDao = authDao;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader("Token");
//        System.out.println(token);
        System.out.println(request.getServletPath()+"?"+request.getQueryString());
        if (StringUtils.isEmpty(token)){
            response.setStatus(105);
            return false;
        }
        Users user = authDao.findUserByToken(token);
//        System.out.println(user);
        if (user == null){
            response.setStatus(106);
            return false;
        }
//        System.out.println();
        request.setAttribute("user", JSONObject.toJSONString(user));
        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }
}