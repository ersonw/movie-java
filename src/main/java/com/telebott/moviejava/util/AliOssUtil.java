package com.telebott.moviejava.util;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.telebott.moviejava.service.SystemConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AliOssUtil {
    private static AliOssUtil self;
    @Autowired
    private SystemConfigService configService;
    private static DefaultProfile profile;
    private static IAcsClient client;
    private static String accessKey;
    private static String accessSecret;
    private static String regionld;
    private static String roleArn;
    private static String roleSessionName;
    @PostConstruct
    public void init(){
        self = this;
        rest();
    }
    public static void rest(){
        accessKey = self.configService.getValueByKey("accessKey_ram");
        accessSecret = self.configService.getValueByKey("accessSecret_ram");
//        accessKey = self.configService.getValueByKey("accessKey");
//        accessSecret = self.configService.getValueByKey("accessSecret");
        regionld = self.configService.getValueByKey("regionld");
        roleArn = self.configService.getValueByKey("roleArn");
        roleSessionName = self.configService.getValueByKey("roleSessionName");
        if (StringUtils.isEmpty(accessKey) ||
                StringUtils.isEmpty(accessSecret) ||
                StringUtils.isEmpty(regionld) ||
                StringUtils.isEmpty(roleArn) ||
                StringUtils.isEmpty(roleSessionName)
        ){
            return;
        }
        //构建一个阿里云客户端，用于发起请求。
        //构建阿里云客户端时需要设置AccessKey ID和AccessKey Secret。
        profile = DefaultProfile.getProfile(regionld, accessKey, accessSecret);
        client = new DefaultAcsClient(profile);
        System.out.println("重置Oss授权成功!");
    }
    public static String getToken(){
        //构造请求，设置参数。关于参数含义和设置方法，请参见《API参考》。
        AssumeRoleRequest request = new AssumeRoleRequest();
        request.setSysRegionId(regionld);
        request.setRoleArn(roleArn);
        request.setRoleSessionName(roleSessionName);

        //发起请求，并得到响应。
        try {
            AssumeRoleResponse response = client.getAcsResponse(request);
//            System.out.println();
            JSONObject object = JSONObject.parseObject(JSONObject.parseObject(new Gson().toJson(response)).get("credentials").toString());
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SecurityToken", object.get("securityToken").toString());
            jsonObject.put("AccessKeyId", object.get("accessKeyId").toString());
            jsonObject.put("AccessKeySecret", object.get("accessKeySecret").toString());
            jsonObject.put("Expiration", object.get("expiration").toString());
            return jsonObject.toString();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
        return null;
    }
}
