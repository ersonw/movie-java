package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "withdrawal_cards")
@Cacheable
@ToString(includeFieldNames = true)
public class WithdrawalCards {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String bank;
    private String code;
    private long uid;
    private long addTime;
    private long updateTime;
}
