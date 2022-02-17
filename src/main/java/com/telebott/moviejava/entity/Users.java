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
    private long id;
    private String nickname;
    private int sex;
    private long birthday;
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
    private String uid;
    private String identifier;
    private long superior;
    @Transient
    private String token;
}
