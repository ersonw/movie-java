package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultData {
    private int code = 200;
    private JSONObject data = null;
    private String message = "";
}