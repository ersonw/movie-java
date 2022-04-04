package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "online_pay")
@Cacheable
@ToString(includeFieldNames = true)
public class OnlinePay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    private String iconImage;
    private int status;
    private long ctime;
    private long utime;
    private long type;
}
