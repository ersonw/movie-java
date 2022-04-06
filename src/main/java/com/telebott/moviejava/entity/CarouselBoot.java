package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "carousel_boot")
@Cacheable
@ToString(includeFieldNames = true)
public class CarouselBoot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String image;
    private int du;
    private int status;
    private long addTime;
    private long updateTime;
}
