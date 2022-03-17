package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "user_posts")
@Cacheable
@ToString(includeFieldNames = true)
public class UserPosts {
    @Id
    @GeneratedValue
    private long id;
    private String title;
    private String context;
    private String images;
    private long uid;
    private long add_time;
    private int status;
}
