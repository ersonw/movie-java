package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "moblie_config")
@Cacheable
@ToString(includeFieldNames = true)
public class MoblieConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String version;
    private String hash;
    private int forces;
    private int autoLogin;
    private String text;
    private long addTime;
    private long updateTime;
}
