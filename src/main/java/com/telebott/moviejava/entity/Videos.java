package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "videos")
@Cacheable
@ToString(includeFieldNames = true)
public class Videos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String title;
    private String picThumb;
    private String gifThumb;
    private long vodTimeAdd;
    private long vodTimeUpdate;
    private long vodClass;
    private long vodDuration;
    private String vodPlayUrl;
    private String vodContent;
    private String vodDownUrl;
    private String vodTag;
    private long actor;
    private long diamond;
    private int status;
    private long collects;
    private long recommends;
    private String shareId;
}
