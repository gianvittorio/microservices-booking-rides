package com.gianvittorio.orderservice.service;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import reactor.core.publisher.Mono;

public interface OrdersService {

    Mono<OrderEntity> createOrder(final OrderEntity orderEntity);
}
