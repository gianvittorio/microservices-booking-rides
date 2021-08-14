package com.gianvittorio.orderservice.web.controller;

import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import com.gianvittorio.orderservice.web.dto.OrderResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("orders")
public class OrderController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<OrderResponseDTO> createOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {
        return Mono.empty();
    }
}
