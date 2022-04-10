package com.telebott.moviejava.util;

import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.data.wData;
import com.telebott.moviejava.service.WaLiConfigService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.annotation.PostConstruct;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
@Component
public class WaLiUtil {
    private static WaLiUtil self;
    private static final int TIME_OUT = 30;
    public static final String TRANSFER_V2 = "transferV2";
    public static final String QUERY_ORDER_V2 = "queryOrderV2";
    public static final String GET_AGENT_BALANCE = "getAgentBalance";
    public static final String REGISTER = "register";
    public static final String ENTER_GAME = "enterGame";
    public static final String KICK = "kick";
    public static final String GET_BALANCE = "getBalance";
    public static final String GET_RECORD_V2 = "getRecordV2";

    @Autowired
    private WaLiConfigService waLiConfigService;
    static String apiUrl;
    static String agentId;
    static String apiUser;
    static String encryptKey;
    static String signKey;

    public static boolean tranfer(long id, long balance) {
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid=23porn_"+id+"&credit="+(balance / 100d)+"&orderId="+agentId+"_"+TimeUtil._getOrderNo()+"_23porn_"+ id;
        p = encrypt(encryptKey,p);
        String sign = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",sign);
        String result = sendGet(apiUrl+"/"+TRANSFER_V2,map);
//        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getObject().getInteger("status") == 1){
                        return true;
                    }else{
                        System.out.println("UID:"+id+" 划转失败! 金额"+ (balance / 100d));
                    }
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return false;
    }

    @PostConstruct
    public void init(){
        self = this;
        rest();
    }
    public static void rest(){
        apiUrl = (self.waLiConfigService.getValueByName("apiUrl"));
        agentId = (self.waLiConfigService.getValueByName("agentId"));
        apiUser = (self.waLiConfigService.getValueByName("apiUser"));
        encryptKey = (self.waLiConfigService.getValueByName("encryptKey"));
        signKey = (self.waLiConfigService.getValueByName("signKey"));
        if (StringUtils.isNotEmpty(apiUrl) &&
                StringUtils.isNotEmpty(agentId) &&
                StringUtils.isNotEmpty(apiUser) &&
                StringUtils.isNotEmpty(encryptKey) &&
                StringUtils.isNotEmpty(signKey)
        ) {
            System.out.println("瓦力游戏配置加载成功！");
        }
    }
    private static String doPost(String url, Map<String, String> map) throws Exception {
        HttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        try {
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + "UTF-8");
            RequestConfig build = RequestConfig.custom().setConnectTimeout(10000).build();
            httpPost.setConfig(build);
            //设置参数
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            Iterator iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> elem = (Map.Entry<String, String>) iterator.next();
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            HttpResponse response = httpClient.execute(httpPost);
            if (response != null) {
                org.apache.http.HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "UTF-8");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //throw new Exception();
        }
        return result;
    }
    private static String sendGet(String httpUrl, Map<String, String> parameter) {
        if (parameter == null || httpUrl == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = parameter.entrySet().iterator();
        while (iterator.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            String value;
            try {
                value = URLEncoder.encode(entry.getValue(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                value = "";
            }
            sb.append(key).append('=').append(value);
        }
        String urlStr = null;
        if (httpUrl.lastIndexOf('?') != -1) {
            urlStr = httpUrl + '&' + sb.toString();
        } else {
            urlStr = httpUrl + '?' + sb.toString();
        }

        HttpURLConnection httpCon = null;
        String responseBody = null;
        try {
            URL url = new URL(urlStr);
            httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("GET");
            httpCon.setConnectTimeout(TIME_OUT * 1000);
            httpCon.setReadTimeout(TIME_OUT * 1000);
            // 开始读取返回的内容
            InputStream in = httpCon.getInputStream();
            byte[] readByte = new byte[1024];
            // 读取返回的内容
            int readCount = in.read(readByte, 0, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (readCount != -1) {
                baos.write(readByte, 0, readCount);
                readCount = in.read(readByte, 0, 1024);
            }
            responseBody = new String(baos.toByteArray(), "UTF-8");
            baos.close();
        } catch (Exception ignored) {
        } finally {
            if (httpCon != null)
                httpCon.disconnect();
        }
        return responseBody;
    }
    private static String encrypt(String key, String src){
        String p = null;
//        String src = "name=Alice&text=Hello";
        byte[] aesKey = key.getBytes(StandardCharsets.UTF_8);
        try{
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(aesKey, "AES"));
            byte[] encrypted = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
            p = Base64.getEncoder().encodeToString(encrypted);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return p;
    }
    private static String getSign(String key, String p, int t){
        return DigestUtils.md5DigestAsHex((p + t + key).getBytes());
    }
    private static void handlerError(String msg){
        switch(msg){
            case "illegal_a":
                System.out.println("a 参数异常。请检查“API 账号”是否正确。");
                break;
            case "illegal_t":
                System.out.println("t 参数异常。请检查：\n" +
                        "1. 时间格式是否正确，时间单位应为秒，⽽⾮毫秒。\n" +
                        "2. 调⽤环境的系统时间是否准确。系统时间偏差不应超过 1 分\n" +
                        "钟。\n" +
                        "3. 调⽤环境的系统时区设置是否与预期⼀致。");
                break;
            case "illegal_p__base64":
                System.out.println("p 参数不是有效的 Base64。请检查：\n" +
                        "拼接 url 时， p 参数是否有 url 转义处理（url encode）。\n" +
                        "Base64 中可能有 / + = 符号，如不转移，会影响服务器读取。\n");
                break;
            case "illegal_p__aes":
                System.out.println("p 参数 AES 解密失败。");
                break;
            case "illegal_k":
                System.out.println("k 参数异常。签名不对。");
                break;
            case "illegal_src_ip":
                System.out.println("IP ⽩名单拦截。\n" +
                        "请检查后台配置的 IP ⽩名单是否正确。\n" +
                        "如刚调整过配置，需要约2分钟⽣效。");
                break;
            case "illegal_act":
                System.out.println("没有这个接⼝。");
                break;
            case "too_many_requests":
                System.out.println("接⼝调⽤过于频繁，应降低请求频率。");
                break;
            case "internal_error":
                System.out.println("服务器内部错误。");
                break;
            case "uid_required":
                System.out.println("缺少业务参数 uid");
                break;
            case "credit_illegal":
                System.out.println("业务参数 credit 格式不对\n");
                break;
        }
    }
    public static void test(){
        System.out.println(enterGame(2,1));
//        int t = (int) (System.currentTimeMillis() / 1000);
//        Map<String, String> map = new HashMap<>();
//        map.put("a",apiUser);
//        map.put("t", String.valueOf(t));
////        String ping = "uid=23porn_1";
//        String ping = "uid=23porn_1&credit=-100&orderId="+agentId+"_"+TimeUtil._getOrderNo()+"_1";
//        String p = encrypt(encryptKey,ping);
//        String sign = getSign(signKey,p,t);
//        map.put("p",p);
//        map.put("k",sign);
////        System.out.println(sendGet(apiUrl+"/"+ENTER_GAME,map));
//        System.out.println(sendGet(apiUrl+"/"+TRANSFER_V2,map));
    }
    public static Double getBalance(long uid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid=23porn_"+uid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+GET_BALANCE,map);
//        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getBalance().getStatus() == -1){
                        register(uid);
                    }
                    return data.getBalance().getBalance();
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return 0.0;
    }
    public static boolean register(long uid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid=23porn_"+uid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+REGISTER,map);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    if (data.getObject().getInteger("status") == 1){
                        return true;
                    }else{
                        System.out.println("UID:"+uid+" ⽤户注册失败!");
                    }
                }else{
                    handlerError(data.getMsg());
                }
            }
        }
        return false;
    }
    public static JSONObject enterGame(long uid, int gid){
        int t = (int) (System.currentTimeMillis() / 1000);
        Map<String, String> map = new HashMap<>();
        map.put("a",apiUser);
        map.put("t", String.valueOf(t));
        String p = "uid=23porn_"+uid+"&game="+gid;
        p = encrypt(encryptKey,p);
        String k = getSign(signKey,p,t);
        map.put("p",p);
        map.put("k",k);
        String result = sendGet(apiUrl+"/"+ENTER_GAME,map);
//        System.out.println(result);
        if (result != null && result.startsWith("{")){
            wData data = JSONObject.toJavaObject(JSONObject.parseObject(result),wData.class);
            if (data != null) {
                if (data.getCode() == 0){
                    return data.getObject();
                }else{
                    handlerEnterGameError(data.getMsg());
                }
            }
        }
        return null;
    }
    private static void handlerEnterGameError(String msg) {
        switch (msg){
            case "game_requests":
                System.out.println("game 参数为空，如果不想选择进⼊游戏，game的key和value都不需要传");
                break;
            case "orderId_requests":
                System.out.println("orderId 参数为空，如果不想进⾏划拨，orderId的key和value都不需要");
                break;
        }
    }
}

