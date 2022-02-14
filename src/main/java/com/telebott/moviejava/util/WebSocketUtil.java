package com.telebott.moviejava.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WebSocketUtil {
    public static final int login = 100;
    public static final int login_success = 101;
    public static final int login_fail = 102;
    public static final int message_kefu_sending = 103;
    public static final int message_kefu_send_success = 104;
    public static final int message_kefu_send_fail = 105;
    public static final int message_kefu_recevie = 106;
    public static final int user_change = 107;
    public static final int user_change_fail = 108;
    public static final int user_change_success = 109;
    public static final int user_change_passwoed = 110;
    public static final int user_change_passwoed_fail = 111;
    public static final int user_change_passwoed_success = 112;
}
