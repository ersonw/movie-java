package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "marquee")
@Cacheable
@ToString(includeFieldNames = true)
public class Marquee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;
    private long addTime;
}
