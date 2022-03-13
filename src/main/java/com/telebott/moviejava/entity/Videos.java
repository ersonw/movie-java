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
public class Videos implements Comparable<Videos> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long uid;
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
    private long play;
    private long recommends;
    private String shareId;
    private String numbers;

    @Override
    public int compareTo(Videos video) {
//        int result = Long.valueOf(this.play - video.getPlay()).intValue();
        int result = Long.valueOf(video.getPlay() - this.play).intValue();
        if (0 == result){
            result = this.title.compareTo(video.title);
//            result = video.title.compareTo(this.title);
        }
        return result;
    }
}
