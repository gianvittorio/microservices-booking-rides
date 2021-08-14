package com.gianvittorio.orderservice.config;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.reactivestreams.Publisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class R2DBCConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactories.get("r2dbc:h2:mem:///orders");
    }

    @Bean
    public Publisher<? extends Connection> connectionPublisher(final ConnectionFactory connectionFactory) {
        return connectionFactory.create();
    }
}
