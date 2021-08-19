package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.repository.OrdersRepository;
import com.gianvittorio.orderservice.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrderServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    @Override
    public Mono<OrderEntity> save(final OrderEntity orderEntity) {

        return ordersRepository.save(orderEntity);
    }

    @Override
    public Flux<OrderEntity> findByStatusAndDepartureTimeBetween(final String status, final LocalDateTime start, final LocalDateTime end) {
        return ordersRepository.findByStatusAndDepartureTimeBetween(status, start, end);
    }
}
