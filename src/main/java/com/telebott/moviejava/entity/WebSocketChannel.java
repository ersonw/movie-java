package com.telebott.moviejava.entity;

import com.telebott.moviejava.bootstrap.ServerWebSocket;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.UUID;

@Getter
@Setter
@ToString
public class WebSocketChannel {
    private String id;
    private String name;

    public WebSocketChannel(){
        UUID uuid = UUID.randomUUID();
        id = uuid.toString().replaceAll("-","");
        name = RandomStringUtils.randomAlphanumeric(16);
    }
}
