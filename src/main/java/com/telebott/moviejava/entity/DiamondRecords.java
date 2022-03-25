package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "diamond_records")
@Cacheable
@ToString(includeFieldNames = true)
public class DiamondRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long uid;
    private long diamond;
    private long addTime;
    private long updateTime;
    private int status;
    private String reason;
}
