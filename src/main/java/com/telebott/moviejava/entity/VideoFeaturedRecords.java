package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "video_featured_records")
@Cacheable
@ToString(includeFieldNames = true)
public class VideoFeaturedRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    private long fid;
    private long vid;
    private long addTime;
    public VideoFeaturedRecords(VideoFeaturedRecords records){
        this.id = records.getId();
        this.fid = records.getFid();
        this.vid = records.getVid();
        this.addTime = records.getAddTime();
    }
    public VideoFeaturedRecords(){
        super();
    }
}
