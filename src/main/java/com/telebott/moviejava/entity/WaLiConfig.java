package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "wali_config")
@Cacheable
@ToString(includeFieldNames = true)
public class WaLiConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String val;
    private long addTime;
    private long updateTime;
}
