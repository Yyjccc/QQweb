package com.fas.config;

import com.fas.handler.PreInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Slf4j
@EnableAsync
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    private final PreInterceptor preInterceptor;

    public MvcConfig(PreInterceptor preInterceptor) {
        this.preInterceptor = preInterceptor;
    }

    // 公共请求头
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.addPathPrefix("/api", c -> true);
    }

    // 拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(preInterceptor).addPathPatterns("/**").excludePathPatterns("/chat");
    }
}
