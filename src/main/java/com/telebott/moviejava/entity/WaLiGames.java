package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "wali_games")
@Cacheable
@ToString(includeFieldNames = true)
public class WaLiGames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String image;
    private int gameId;
    private int status;
    private long addTime;
    private long updateTime;
}
