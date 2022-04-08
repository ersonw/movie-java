package com.telebott.moviejava.data;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class wData {
    private int code;
    private String msg;
    private String data;

    public JSONObject getObject(){
        return JSONObject.parseObject(data);
    }
    public wBalance getBalance(){
        return JSONObject.toJavaObject(getObject(),wBalance.class);
    }
}
