package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "game_cash_in_orders")
@Cacheable
@ToString(includeFieldNames = true)
public class GameCashInOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long uid;
    private String orderId;
    private long amount;
    private long cid;
    private long addTime;
    private long updateTime;
    private int status;
}
