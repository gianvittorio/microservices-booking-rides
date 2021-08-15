package com.gianvittorio.usersservice.config.r2dbc.postgres;

import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Profile("docker")
@Configuration
@EnableConfigurationProperties(R2BCPostgresConfigurationProperties.class)
@RequiredArgsConstructor
public class R2DBCPostgresFactoryConfiguration {

    private final R2BCPostgresConfigurationProperties properties;

    @Bean
    public Builder connectionFactoryOptions() {

        int port;
        try {
            port = Integer.parseInt(properties.getPort());
        } catch (Exception e) {
            port = 5432;
        }

        return ConnectionFactoryOptions.builder()
                .option(DRIVER, properties.getDriver())
                .option(HOST, properties.getHost())
                .option(PORT, port)
                .option(USER, properties.getUser())
                .option(PASSWORD, properties.getPassword())
                .option(DATABASE, properties.getDatabase());
    }
}
