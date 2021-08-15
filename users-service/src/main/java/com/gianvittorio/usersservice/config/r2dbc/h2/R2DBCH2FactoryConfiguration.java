package com.gianvittorio.usersservice.config.r2dbc.h2;

import io.r2dbc.spi.ConnectionFactoryOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Profile("!docker")
@Configuration
@EnableConfigurationProperties(R2BCH2ConfigurationProperties.class)
@RequiredArgsConstructor
public class R2DBCH2FactoryConfiguration {

    private final R2BCH2ConfigurationProperties properties;

    @Bean
    public ConnectionFactoryOptions.Builder connectionFactoryOptions() {
        return ConnectionFactoryOptions.builder()
                .option(DRIVER, properties.getDriver())
                .option(PROTOCOL, properties.getProtocol())
                .option(USER, properties.getUser())
                .option(PASSWORD, properties.getPassword())
                .option(DATABASE, properties.getDatabase());
    }
}
