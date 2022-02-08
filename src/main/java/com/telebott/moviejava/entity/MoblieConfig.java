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
    @Column(name = "id")
    private int id;
    private String version;
    private String hash;
    private int autoLogin;
    private String bootImage;
    private String url;
    private int forces;
    private Long ctime;
    private Long utime;
}
