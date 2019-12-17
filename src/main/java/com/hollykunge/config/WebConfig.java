package com.hollykunge.config;

import com.github.hollykunge.security.interceptor.SSOUserAuthRestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Autowired
    private SSOUserAuthRestInterceptor ssoUserAuthRestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ssoUserAuthRestInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/index","/login");
        super.addInterceptors(registry);
    }

}