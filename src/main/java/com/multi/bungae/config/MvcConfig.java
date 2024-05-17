package com.multi.bungae.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 프로젝트 루트에 위치한 uploads 폴더를 정적 리소스로 추가
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///Users/jeonghyeyeon/Desktop/multicam/spring-workspace/bungae/uploads/");
    }
}