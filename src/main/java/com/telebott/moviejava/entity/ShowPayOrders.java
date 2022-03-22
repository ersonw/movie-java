package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "show_pay_orders")
@ToString(includeFieldNames = true)
@Setter
@Getter
public class ShowPayOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String orderId;
    private String orderNo;
    private String tradeNo;
    private long amount;
    private long addTime;
    private int status;
}
