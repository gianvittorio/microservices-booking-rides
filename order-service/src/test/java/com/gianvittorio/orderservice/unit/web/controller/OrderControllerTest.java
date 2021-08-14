package com.gianvittorio.orderservice.unit.web.controller;

import com.gianvittorio.common.domain.Category;
import com.gianvittorio.orderservice.web.controller.OrderController;
import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = OrderController.class)
@TestPropertySource(properties = "server.port=8081")
public class OrderControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    @DisplayName("Just a silly test")
    public void givenValidRequestthenReturnValidResponse() {

        // Given
        final OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                .document("05306757901")
                .category(Category.COMFORT)
                .origin("X")
                .destination("Y")
                .departureTime(ZonedDateTime.now().plusMinutes(30))
                .build();


        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("orders")
                .build()
                .toUriString();

        webTestClient.post()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(Mono.justOrEmpty(orderRequestDTO), OrderRequestDTO.class)
                .exchange()
                .expectStatus().isOk();
    }
}
