package com.gianvittorio.orderservice.web.controller;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.service.OrdersService;
import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import com.gianvittorio.orderservice.web.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    @GetMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public Mono<String> helloWorld() {
        return Mono.just("hello world!!!");
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderResponseDTO> createOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {

        OrderEntity orderEntity = OrderEntity.builder()
                .passengerId(123l)
                .driverId(321l)
                .origin("X")
                .destination("Y")
                .departureTime(LocalDateTime.now().plusMinutes(30))
                .build();

        return ordersService.createOrder(orderEntity)
                .map(entity ->
                        OrderResponseDTO.builder()
                                .orderId(entity.getId())
                                .createdAt(LocalDateTime.now())
                                .departureTime(entity.getDepartureTime())
                                .driverName("ciccio broccolo")
                                .build()
                );
    }
}
