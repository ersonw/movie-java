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
    @GeneratedValue
    private long id;
    private String nickname;
    private String bkImage;
    private String signature;
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
    private String email;
    private String invite;
    private String avatar;
    private String uid;
    private String identifier;
    private long superior;
    private long experience;
    private long expireds;
    @Transient
    private String token;
}
