package com.innrate.common.config.jpa;

import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyComponentPathImpl;
import org.springframework.context.annotation.Bean;

public class InnrateJpaConfig {
    @Bean
    public ImplicitNamingStrategy implicitNamingStrategy() {
        return new ImplicitNamingStrategyComponentPathImpl();
    }
}
