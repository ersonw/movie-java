package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@ToString
public class UploadData {
    private boolean last;
    private String id;
    private MultipartFile file;
    private String user;

    public Users getUser() {
        JSONObject object = JSONObject.parseObject(user);
        return JSONObject.toJavaObject(object,Users.class);
    }
}
