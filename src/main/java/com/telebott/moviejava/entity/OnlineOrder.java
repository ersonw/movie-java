package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "online_order")
@Cacheable
@ToString(includeFieldNames = true)
public class OnlineOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private int type;
    private int cid;
    private long amount;
    private long ctime;
    private long utime;
    private int status;
}
