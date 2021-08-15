package com.gianvittorio.orderservice.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile("!docker")
@ConfigurationProperties(prefix = "app.r2dbc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class R2BCConfigurationProperties {

    private String driver;
    private String protocol;
    private String user;
    private String password;
    private String database;
}
