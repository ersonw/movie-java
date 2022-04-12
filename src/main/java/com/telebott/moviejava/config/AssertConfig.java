package com.telebott.moviejava.config;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.service.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class AssertConfig implements WebMvcConfigurer {
    @Autowired
    private AuthDao authDao;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/").addResourceLocations( "classpath:/META-INF/static/").addResourceLocations( "classpath:/WEB-INF/classes/static/");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exclude = new ArrayList<>();
        exclude.add("/fonts/**");
        exclude.add("/static/**");
        exclude.add("/css/**");
        exclude.add("/js/**");
        exclude.add("/img/**");
        exclude.add("/api/user/info");
        exclude.add("/api/user/login");
        exclude.add("/api/user/bindPhone");
        exclude.add("/api/video/hotTags");
        exclude.add("/*");
        exclude.add("/api/*");
        AuthInterceptor authInterceptor = new AuthInterceptor(authDao);
        registry.addInterceptor(authInterceptor).addPathPatterns("/**")
                .excludePathPatterns(exclude);
    }
}
