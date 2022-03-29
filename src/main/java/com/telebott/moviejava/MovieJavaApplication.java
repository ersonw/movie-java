package com.telebott.moviejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import javax.swing.*;

@SpringBootApplication
public class MovieJavaApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application){
        return application.sources(MovieJavaApplication.class);
    }
    public static void main(String[] args) {
        SpringApplication.run(MovieJavaApplication.class, args);
//        System.out.println("我是DemoController执行中...");
    }

}
