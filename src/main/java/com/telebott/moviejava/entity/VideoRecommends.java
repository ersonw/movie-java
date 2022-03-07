package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_recommends")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoRecommends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long uid;
    private long vid;
    private String reason;
    private int status;
    private long addTime;
}
