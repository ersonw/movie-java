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
    private long vodTimeAdd;
    private long vodTime;
    private int vodClass;
    private long vodDuration;
    private String vodPlayUrl;
    private String vodContent;
    private String vodDownUrl;
    private String vodTag;
    private long actor;
    private long diamond;
    private int status;
}
