package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "gold_records")
@Cacheable
@ToString(includeFieldNames = true)
public class GoldRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long uid;
    private long gold;
    private long addTime;
    private long updateTime;
    private int status;
    private String reason;
}
