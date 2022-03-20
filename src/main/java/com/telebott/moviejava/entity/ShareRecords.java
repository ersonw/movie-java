package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "share_records")
@ToString(includeFieldNames = true)
@Setter
@Getter
public class ShareRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int type;
    private long amount;
    private int status;
    private long addTime;
    private String reason;
    private long uid;
    private long toUid;
}
