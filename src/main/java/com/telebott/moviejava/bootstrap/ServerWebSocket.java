package com.telebott.moviejava.bootstrap;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.RedisDao;
import com.telebott.moviejava.entity.KeFuMessage;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.entity.WebSocketChannel;
import com.telebott.moviejava.util.WebSocketUtil;
import com.alibaba.fastjson.JSONObject;
import com.telebott.moviejava.entity.WebSocketData;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/")
@Component
@Getter
public class ServerWebSocket {
    @Autowired
    private AuthDao authDao;
    @Autowired
    private RedisDao redisDao;
    private static ServerWebSocket self;
    /**
     * 在线人数
     */
    public static int onlineNumber = 0;

    /**
     * 所有的对象
     */
    public static List<ServerWebSocket> webSockets = new CopyOnWriteArrayList<ServerWebSocket>();
    public static List<WebSocketChannel> webSocketChannels = new ArrayList<>();
    /**
     * 会话
     */
    private Session session;
    private String token;
    private Users user;
    private final Timer timer = new Timer();

    @PostConstruct
    public void init() {
//        System.out.println("websocket 加载");
        self = this;
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * 建立连接
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        onlineNumber++;
        webSockets.add(this);
        this.session = session;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage("H");
            }
        }, 500, 1000 * 10);
        System.out.println("session:" + session.getId() + " 当前在线人数" + onlineNumber);
    }

    private void _checkReconnet() {
        if (user == null || user.getId() == 0) return;
        for (WebSocketChannel channel: webSocketChannels) {
            for (int uid: channel.getUsers()) {
                if (uid == user.getId()){

                }
            }
        }
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        webSockets.remove(this);
        timer.cancel();
        System.out.println("有连接关闭！ 当前在线人数" + onlineNumber);
    }

    /**
     * 收到客户端的消息
     *
     * @param message 消息
     * @param session 会话
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        System.out.println(message);
        JSONObject object = JSONObject.parseObject(message);
        if (object == null) return;
        WebSocketData data = JSONObject.toJavaObject(object, WebSocketData.class);
        System.out.println(data.getData());
        handleMessages(data);
    }

    private void handleMessages(WebSocketData webSocketData) {
        JSONObject data = JSONObject.parseObject(webSocketData.getData());
        switch (webSocketData.getCode()) {
            case WebSocketUtil.login:
                handleLogin(data);
                break;
            case WebSocketUtil.message_kefu_sending:
                handlerKeFuMessage(data);
                break;
            default:
                webSocketData.setMessage("未识别消息!");
                sendMessage(webSocketData);
                break;
        }
    }

    private void handlerKeFuMessage(JSONObject object) {
        WebSocketData data = new WebSocketData();
        KeFuMessage message = JSONObject.toJavaObject(object, KeFuMessage.class);
        String id = message.getId();
        if (user == null || user.getId() == 0) {
            message = new KeFuMessage();
            message.setId(id);
            data.setCode(WebSocketUtil.message_kefu_send_fail);
            data.setMessage("未绑定手机号或未注册用户部分功能受限！");
            data.setData(JSONObject.toJSONString(message));
            sendMessage(data);
            return;
        }
        message.setUid(user.getId());
        self.redisDao.putKeFuMessage(message);
        message = new KeFuMessage();
        message.setId(id);
        data.setCode(WebSocketUtil.message_kefu_send_success);
        data.setData(JSONObject.toJSONString(message));
        sendMessage(data);
    }

    private void handleLogin(JSONObject object) {
        WebSocketData data = new WebSocketData();
        if (object != null && object.get("token") != null) {
            String token = object.get("token").toString();
            Users users = self.authDao.findUserByToken(token);
            if (users != null) {
                this.token = token;
                this.user = users;
                data.setCode(WebSocketUtil.login_success);
                sendTo(data);
                return;
            }
        }
        data.setCode(WebSocketUtil.login_fail);
        sendTo(data);
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(WebSocketData message) {
        sendMessage(JSONObject.toJSONString(message));
    }

    public void sendMessage(String message) {
        if (session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息至指定在线人
     *
     * @param message
     * @param token
     */
    public static void sendTo(WebSocketData message, String token) {
        try {
            for (ServerWebSocket socket : webSockets) {
                if (socket.token.equals(token)) {
                    socket.sendMessage(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送消息至所有人
     *
     * @param message
     */
    public static void sendTo(WebSocketData message) {
        try {
            for (ServerWebSocket socket : webSockets) {
                socket.sendMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}