package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "commodity_vip")
@Cacheable
@ToString(includeFieldNames = true)
public class CommodityVip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private int type;
    private String title;
    private String describes;
    private String image;
    private String currency;
    private int original;
    private int amount;
    private long ctime;
    private long utime;
    private int status;
    private int isText;
    private String addTime;
}
