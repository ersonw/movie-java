package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class RequestData {
    @JsonProperty(value = "id", required = false)
    private Integer id;
    @JsonProperty(value = "user", required = false)
    private String user;
    public Users getUser() {
        JSONObject jsonObject = JSONObject.parseObject(user);
        return JSONObject.toJavaObject(jsonObject, Users.class);
    }
}
