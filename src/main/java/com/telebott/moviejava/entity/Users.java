package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "users")
@Cacheable
@ToString(includeFieldNames = true)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private long utime;
    private long ctime;
    private long gold;
    private long diamond;
    private int status;
    private String phone;
    private String invite;
    private String avatar;
    @Transient
    private String token;
}
