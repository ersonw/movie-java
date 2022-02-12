package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KeFuMessage {
    private String id;
    private String text;
    private String image;
    private long uid;
}
