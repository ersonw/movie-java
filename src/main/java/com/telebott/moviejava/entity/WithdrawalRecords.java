package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "withdrawal_records")
@Cacheable
@ToString(includeFieldNames = true)
public class WithdrawalRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long amount;
    private String reason;
    private String orderNo;
    private long cid;
    private long uid;
    private int status;
    private long addTime;
    private long updateTime;
}
