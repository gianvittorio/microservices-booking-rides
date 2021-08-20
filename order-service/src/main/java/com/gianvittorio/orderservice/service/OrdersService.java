package com.gianvittorio.orderservice.service;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface OrdersService {

    Mono<OrderEntity> findById(final Long id);

    Mono<OrderEntity> save(final OrderEntity orderEntity);

    Flux<OrderEntity> findByStatusAndDepartureBetween(final String status, final LocalDateTime start, final LocalDateTime end);

    Mono<Void> deleteById(final Long id);
}
