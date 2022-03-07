package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_actors")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoActors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private String name;
    private String avatar;
    private String measurements;
    private int status;
    private long addTime;
    private long updateTime;
}
