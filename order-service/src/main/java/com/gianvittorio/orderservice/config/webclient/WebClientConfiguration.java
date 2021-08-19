package com.gianvittorio.orderservice.config.webclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gianvittorio.common.config.SerdesConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@Import(SerdesConfiguration.class)
@RequiredArgsConstructor
public class WebClientConfiguration {

    @Value("${app.api-gateway.host}")
    private String host;

    @Bean
    public WebClient webClient(final ExchangeStrategies exchangeStrategies) {

        final var webClient = WebClient.builder()
                .baseUrl(host)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .exchangeStrategies(exchangeStrategies)
                .build();

        return webClient;
    }

    @Bean
    public ExchangeStrategies exchangeStrategies(final ObjectMapper mapper) {
        var exchangeStrategies = ExchangeStrategies.builder()
                .codecs(
                        clientCodecConfigurer -> {
                            clientCodecConfigurer
                                    .defaultCodecs().
                                    jackson2JsonEncoder(new Jackson2JsonEncoder(mapper, MediaType.APPLICATION_JSON));
                            clientCodecConfigurer
                                    .defaultCodecs()
                                    .jackson2JsonDecoder(new Jackson2JsonDecoder(mapper, MediaType.APPLICATION_JSON));
                        }
                )
                .build();

        return exchangeStrategies;
    }
}
