package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_shares")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoShares {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long uid;
    private long toUid;
    private long vid;
    private long addTime;
}
