package com.telebott.moviejava.util;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UrlUtil {
    public static List<String> urls;
    public static void init(List<String> url){
        urls = url;
    }
}
