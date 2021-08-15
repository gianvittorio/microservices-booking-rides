package com.gianvittorio.orderservice.config.r2dbc.postgres;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;

@Profile("docker")
@ConfigurationProperties(prefix = "app.r2dbc")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class R2BCPostgresConfigurationProperties {

    private String driver;
    private String host;
    private String port;
    private String user;
    private String password;
    private String database;
}
