package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "commodity_vip_order")
@Cacheable
@ToString(includeFieldNames = true)
public class CommodityVipOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long ctime;
    private long expired;
    private int status;
}
