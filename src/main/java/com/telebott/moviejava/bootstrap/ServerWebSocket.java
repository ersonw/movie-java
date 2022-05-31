package com.telebott.moviejava.bootstrap;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.RedisDao;
import com.telebott.moviejava.dao.SmsRecordsDao;
import com.telebott.moviejava.entity.KeFuMessage;
import com.telebott.moviejava.entity.Users;
import com.telebott.moviejava.entity.WebSocketChannel;
import com.telebott.moviejava.service.SmsBaoService;
import com.telebott.moviejava.service.SystemConfigService;
import com.telebott.moviejava.service.UserService;
import com.telebott.moviejava.util.MD5Util;
import com.telebott.moviejava.util.SmsBaoUtil;
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
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@ServerEndpoint(value = "/")
@Component
@Getter
public class ServerWebSocket {
    @Autowired
    private AuthDao authDao;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private UserService userService;
    @Autowired
    private SystemConfigService systemConfigService;
    @Autowired
    public SmsRecordsDao smsRecordsDao;
    @Autowired
    private SmsBaoService smsBaoService;
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
        SmsBaoUtil.init(self.smsBaoService,self.smsRecordsDao);
        WebsocketClient.init();
    }

    /**
     * 出现错误
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
//        error.printStackTrace();
        if (user != null){
//            System.out.println("客户端退出异常： 用户昵称："+user.getNickname()+"  用户ID： "+user.getId());
        }
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
        }, 1000, 1000 * 10);
        System.out.println(" 当前实时在线人数" + onlineNumber);
        WebsocketClient.sendOnline(onlineNumber);
    }

    /**
     * 连接关闭
     */
    @OnClose
    public void onClose() {
        onlineNumber--;
        webSockets.remove(this);
        timer.cancel();
//        System.out.println("有连接关闭！ 当前在线人数" + onlineNumber);
        WebsocketClient.sendOnline(onlineNumber);
        if (this.user != null){
            System.out.println("用户"+this.user.getNickname()+" Websocket 已经离线! 用户ID："+this.user.getId());
        }
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
//        System.out.println(data.getData());
        handleMessages(data);
    }

    private void handleMessages(WebSocketData webSocketData) {
        JSONObject data = JSONObject.parseObject(webSocketData.getData());
        if (data == null) return;
        switch (webSocketData.getCode()) {
            case WebSocketUtil.login:
                _handlerLogin(data);
                break;
            case WebSocketUtil.message_kefu_sending:
                _handlerKeFuMessage(data);
                break;
            case WebSocketUtil.user_change:
                _handlerUserChangeProfile(data);
                break;
            case WebSocketUtil.user_change_passwoed:
                _handlerUserChangePassword(data);
                break;
            default:
                webSocketData.setMessage("未识别消息!");
                sendMessage(webSocketData);
                break;
        }
    }

    private void _handlerUserChangePassword(JSONObject data) {
        user = self.authDao.findUserByToken(token);
        WebSocketData msg = new  WebSocketData();
        msg.setCode(WebSocketUtil.user_change_passwoed_fail);
        msg.setMessage("新版本已发布，旧版本接口不再适用哟！请到官网下载最新版本 ");
        sendMessage(msg);
    }

    private void _handlerUserChangeProfile(JSONObject object) {
        WebSocketData data = new WebSocketData();
        data.setCode(WebSocketUtil.user_change_fail);
        data.setMessage("新版本已发布，旧版本接口不再适用哟！请到官网下载最新版本 ");
        sendMessage(data);
    }

    private void _handlerKeFuMessage(JSONObject object) {
        WebSocketData data = new WebSocketData();
        KeFuMessage message = JSONObject.toJavaObject(object, KeFuMessage.class);
        String id = message.getId();
        data.setMessage("新版本已发布，旧版本接口不再适用哟！请到官网下载最新版本 ");
        sendMessage(data);
    }

    private void _handlerLogin(JSONObject object) {
        WebSocketData data = new WebSocketData();
        if (object != null && object.get("token") != null) {
            String token = object.get("token").toString();
            Users users = self.authDao.findUserByToken(token);
            if (users != null) {
//                System.out.println(this.user);
                if (this.user != null && users.getId() != this.user.getId()){
                    System.out.println("用户"+users.getNickname()+" Websocket 登录成功! 用户ID："+users.getId());
                }else if (this.user == null){
                    System.out.println("用户"+users.getNickname()+" Websocket 登录成功! 用户ID："+users.getId());
                }
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
