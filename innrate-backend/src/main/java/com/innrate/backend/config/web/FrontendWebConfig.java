package com.innrate.backend.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

@Configuration
public class FrontendWebConfig implements WebMvcConfigurer {

    private final String frontendPathPrefix;

    public FrontendWebConfig(@Value("${innrate.frontend-path-prefix}") String frontendPathPrefix) {
        this.frontendPathPrefix = frontendPathPrefix;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/favicon.ico") // icon does not need prefix
                .addResourceLocations("classpath:/frontend/favicon.ico");
        registry
                .addResourceHandler(actualPath("/**"))
                .addResourceLocations("classpath:/frontend/");
        registry
                .addResourceHandler(actualPath("/static/js/**"))
                .addResourceLocations("classpath:/frontend/static/js/");
        registry
                .addResourceHandler(actualPath("/static/css/**"))
                .addResourceLocations("classpath:/frontend/static/css/");
    }

    private String actualPath(String path) {
        return frontendPathPrefix + path;
    }

    @Bean
    public ViewResolver viewResolver() {
        UrlBasedViewResolver viewResolver = new UrlBasedViewResolver();
        viewResolver.setViewClass(InternalResourceView.class);
        return viewResolver;
    }
}
