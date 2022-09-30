package com.innrate.common.spring.controller;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Replaces {@code RequestMappingHandlerMapping} by {@link InnrateHandlerMapping}
 */
@Configuration
public class InnrateHandlerMappingConfig {

    @Bean
    public WebMvcRegistrations webMvcRegistrationsHandlerMapping() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new InnrateHandlerMapping();
            }
        };
    }
}
