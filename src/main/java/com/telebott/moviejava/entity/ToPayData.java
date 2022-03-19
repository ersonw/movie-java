package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString(includeFieldNames = true)
public class ToPayData {
    private  String mchid;
    private  String total_fee;
    private  String out_trade_no;
    private  String callback_url;
    private  String notify_url;
    private  String error_url;
    private  String sign;
}
