package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "commodity_diamond")
@Cacheable
@ToString(includeFieldNames = true)
public class CommodityDiamond {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long diamond;
    private long amount;
    private int status;
    private long ctime;
    private long utime;
}
