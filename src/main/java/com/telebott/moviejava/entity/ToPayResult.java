package com.telebott.moviejava.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@ToString(includeFieldNames = true)
public class ToPayResult {
    private int code;
    private String msg;
    private String data;
    private String payUrl;
    private String orderId;

    public String getPayUrl() {
        if (StringUtils.isEmpty(payUrl)){
            JSONObject object = JSONObject.parseObject(data);
            if (object != null && object.get("payUrl") != null) payUrl = object.get("payUrl").toString();
        }
        return payUrl;
    }

    public String getOrderId() {
        if (StringUtils.isEmpty(orderId)){
            JSONObject object = JSONObject.parseObject(data);
            if (object != null && object.get("orderId") != null) orderId = object.get("orderId").toString();
        }
        return orderId;
    }
}
