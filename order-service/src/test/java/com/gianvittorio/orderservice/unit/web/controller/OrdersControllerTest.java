package com.gianvittorio.orderservice.unit.web.controller;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.service.DriversService;
import com.gianvittorio.orderservice.service.OrdersService;
import com.gianvittorio.orderservice.web.controller.OrdersController;
import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = OrdersController.class)
@TestPropertySource(properties = "server.port=8081")
public class OrdersControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    OrdersService ordersService;

    @MockBean
    DriversService driversService;

    @Test
    @DisplayName("Just a silly test")
    public void givenValidRequestthenReturnValidResponse() {

// Given
        final OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                .document("05306757901")
                .category("comfort")
                .origin("X")
                .destination("Y")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .build();

        final OrderEntity orderEntity = OrderEntity.builder()
                .id(1234l)
                .passengerId(123l)
                .driverId(321l)
                .origin("X")
                .destination("Y")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .build();

        given(ordersService.createOrder(any(OrderEntity.class)))
                .willReturn(Mono.just(orderEntity));


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

        verify(ordersService)
                .createOrder(any(OrderEntity.class));
    }
}
