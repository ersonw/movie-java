package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "wali_game_records")
@Cacheable
@ToString(includeFieldNames = true)
public class WaliGameRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long gameId;
    private long uid;
    private long addTime;
}
