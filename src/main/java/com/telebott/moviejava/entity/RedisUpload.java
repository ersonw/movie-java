package com.telebott.moviejava.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@ToString
public class RedisUpload {
    public RedisUpload(){
        restId();
    }
    public void restId(){
        UUID uuid = UUID.randomUUID();
        id = uuid.toString().replaceAll("-","")+System.currentTimeMillis();
    }
    @Id
    private String id;
    private int fileSize;
    private String hash;
    private boolean network;
    private String url;
    private String tempPath;
    private boolean partial;
    private List<String> partPath;
}
