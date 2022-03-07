package com.telebott.moviejava.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class YzmData {
    private String metadata;
    private String tmpl;
    private String picdomain;
    private String infoHash;
    private String shareid;
    private String qrprefix;
    private String title;
    private String result;
    private int sp_status;
    private String mp4domain;
    private String rpath;
    private String orgfile;
    private String domain;
    private int progress;
    private int md5;
}
