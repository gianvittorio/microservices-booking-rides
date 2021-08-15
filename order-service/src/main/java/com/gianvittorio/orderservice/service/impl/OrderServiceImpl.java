package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.repository.OrdersRepository;
import com.gianvittorio.orderservice.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public Mono<OrderEntity> createOrder(OrderEntity orderEntity) {

        return ordersRepository.save(orderEntity);
    }
}
