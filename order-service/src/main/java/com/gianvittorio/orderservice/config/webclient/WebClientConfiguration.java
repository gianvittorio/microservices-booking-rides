package com.gianvittorio.orderservice.config.webclient;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(WebClientConfigurationProperties.class)
@RequiredArgsConstructor
public class WebClientConfiguration {

    private final WebClientConfigurationProperties configurationProperties;

    @Bean
    public WebClient webClient() {

        final var webClient = WebClient.builder()
                .baseUrl(configurationProperties.getHost())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient;
    }
}
