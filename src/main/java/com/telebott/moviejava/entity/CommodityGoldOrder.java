package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "commodity_gold_order")
@Cacheable
@ToString(includeFieldNames = true)
public class CommodityGoldOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long gold;
    private long amount;
    private int status;
    private long ctime;
    private long utime;
    private long uid;
    private String orderId;
    private long cid;
}
