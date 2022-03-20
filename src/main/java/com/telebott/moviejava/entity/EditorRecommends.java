package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "editor_recommends")
@Cacheable
@ToString(includeFieldNames = true)
public class EditorRecommends {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long vid;
    private String title;
    private double face;
    private double funny;
    private double hot;
    private long addTime;
    private int status;
    private long showTime;
}
