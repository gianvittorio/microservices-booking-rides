package com.gianvittorio.orderservice.config.webclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.api-gateway")
@Data
public class WebClientConfigurationProperties {

    private String host;
}
