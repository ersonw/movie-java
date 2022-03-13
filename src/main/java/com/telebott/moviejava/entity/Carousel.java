package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "carousel")
@Cacheable
@ToString(includeFieldNames = true)
public class Carousel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String name;
    private String image;
    private String url;
    private int type;
    private int status;
    private long addTime;
}
