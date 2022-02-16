package com.telebott.moviejava.bootstrap;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.service.BodyRequestWrapper;
import com.telebott.moviejava.service.ParameterRequestWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@WebFilter(filterName = "myFilter", urlPatterns = "/")
@Order(10000)
public class MyFilter implements Filter {
    @Autowired
    private AuthDao authDao;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
//        authDao = new AuthDao();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if(req instanceof HttpServletRequest){
            HttpServletRequest request = (HttpServletRequest) req;
            String contentType = request.getContentType();
            //body形式（json）
//            System.out.println(request.getMethod());
            if (contentType != null && contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
                //获取request的body参数
                String postContent = getBody(request);
                //如果body中存在数据放入HttpServletRequest
                if (StringUtils.isNotEmpty(postContent)) {
                    //修改、新增、删除参数
                    JSONObject jsStr = JSONObject.parseObject(postContent);
                    String token = ((HttpServletRequest) req).getHeader("Token");
//                    System.out.println(token);
                    if (StringUtils.isNotEmpty(token)){
                        Users user = authDao.findUserByToken(token);
                        if (user != null){
                            jsStr.put("user", JSONObject.toJSONString(user));
                        }
                    }
//                    if(jsStr.containsKey("userName")){
//                        jsStr.put("userName", "对用户名进行解密");
//                    }
                    postContent = jsStr.toJSONString();
                    //将参数放入重写的方法中
                    request = new BodyRequestWrapper(request, postContent);
                }
            }
            //form表单形式
            if (contentType != null && (contentType.equals(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    || contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE))
                    && !request.getParameterMap().isEmpty()) {
                //修改、新增、删除参数
                Map<String, String[]> parameterMap = request.getParameterMap();
                //对请求参数进行处理
                String token = ((HttpServletRequest) req).getHeader("Token");
//                System.out.println(token);
                if (StringUtils.isNotEmpty(token)){
                    Users user = authDao.findUserByToken(token);
                    if (user != null){
                        parameterMap.put("user", new String[]{JSONObject.toJSONString(user)});
                    }
                }
                request = new ParameterRequestWrapper(request, parameterMap);
            }
            if (request.getMethod().equals("GET") /*&& !request.getParameterMap().isEmpty()*/){
//                //对请求参数进行处理
                String token = ((HttpServletRequest) req).getHeader("Token");
                if (StringUtils.isNotEmpty(token)){
                    Users user = authDao.findUserByToken(token);
                    if (user != null){
                        Map<String, String[]> parameterMap =new HashMap(request.getParameterMap());
//                        parameterMap.put("user", new String[]{JSON.toJSONString(user)});
//                        parameterMap.put("user", new String[]{user.toString()});
//                        request = new ParameterRequestWrapper(request, parameterMap);
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("user", user);
//                        RequestParameterWrapper wrapper = new RequestParameterWrapper(request);
//                        wrapper.addParameters(map);
                        ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request,parameterMap);
                        wrapper.addParameter("user", JSONObject.toJSONString(user));
                        request = wrapper;
                    }
                }
            }
            chain.doFilter(request, response);
        }else{
            chain.doFilter(req, response);
        }
    }

    //获取Request的body数据
    private String getBody(ServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    @Override
    public void destroy() {

    }
}
