package com.gianvittorio.orderservice.config;

import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import reactor.core.publisher.Mono;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
@EnableConfigurationProperties(R2BCConfigurationProperties.class)
@EnableR2dbcRepositories
@RequiredArgsConstructor
public class R2DBCConfiguration extends AbstractR2dbcConfiguration {

    private final R2BCConfigurationProperties properties;

    @Override
    @Bean
    public ConnectionFactory connectionFactory() {
        return ConnectionFactoryBuilder.withOptions(connectionFactoryOptions()).build();
    }

    @Bean
    public ConnectionFactoryOptions.Builder connectionFactoryOptions() {
        return ConnectionFactoryOptions.builder()
                .option(DRIVER, properties.getDriver())
                .option(PROTOCOL, properties.getProtocol())
                .option(USER, properties.getUser())
                .option(PASSWORD, properties.getPassword())
                .option(DATABASE, properties.getDatabase());
    }

    @Bean
    public ConnectionFactoryInitializer initializer(final ConnectionFactory connectionFactory) {
        final ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        final CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));

        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    @Bean
    public Mono<Connection> connectionMono(final ConnectionFactory connectionFactory) {
        return Mono.from(connectionFactory.create());
    }
}
