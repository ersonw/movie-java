package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "game_balance_orders")
@Cacheable
@ToString(includeFieldNames = true)
public class GameBalanceOrders {
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
