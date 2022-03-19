package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "balance_orders")
@Cacheable
@ToString(includeFieldNames = true)
public class BalanceOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long uid;
    private long amount;
    private int status;
    private long addTime;
    private long updateTime;
    private String reason;
}
