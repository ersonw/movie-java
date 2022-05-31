package com.telebott.moviejava.bootstrap;

import com.alibaba.fastjson.JSONObject;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

/**
 * Created by jack on 2017/10/25.
 */
public class WebsocketClient {
    public static WebSocketClient client;

    private static void _init(){
        try {
            client = new WebSocketClient(new URI("ws://localhost:8016"),new Draft_6455()) {
                @Override
                public void onOpen(ServerHandshake serverHandshake) {
//                    System.out.println("打开链接");
                    client.sendPing();
                    sendOnline(ServerWebSocket.onlineNumber);
                }

                @Override
                public void onMessage(String s) {
//                    System.out.println("收到消息"+s);
                }

                @Override
                public void onClose(int i, String s, boolean b) {
//                    System.out.println("链接已关闭");
                    _init();
                }

                @Override
                public void onError(Exception e) {
//                    e.printStackTrace();
//                    System.out.println("发生错误已关闭");
//                    _init();
                }
            };
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        client.connect();
        System.out.println(client.getDraft());
        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
//            System.out.println("还没有打开");
        }
    }
    public static void init() {
        _init();
//        System.out.println("打开了");
//        try {
//            send("hello world".getBytes("utf-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        client.send("hello world");
//        client.close();
    }
    public static void sendOnline(long c) {
        JSONObject object = new JSONObject();
        object.put("count",c);
        client.send(JSONObject.toJSONString(object));
//        send(JSONObject.toJSONString(object).getBytes(StandardCharsets.UTF_8));
    }

    public static void send(byte[] bytes){
        client.send(bytes);
    }

}