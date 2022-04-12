package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "game_cash_in")
@Cacheable
@ToString(includeFieldNames = true)
public class GameCashIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long amount;
    private int vip;
    private long addTime;
    private long updateTime;
    private int status;
}
