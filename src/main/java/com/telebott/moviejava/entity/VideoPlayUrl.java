package com.telebott.moviejava.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class VideoPlayUrl {
    private long size;
    private String resolution;
    private String url;
}
