package com.gianvittorio.orderservice.repository;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<OrderEntity, Long> {

    Flux<OrderEntity> findByPassengerIdAndDriverId(final Long passengerId, final Long driverId);

    Flux<OrderEntity> findByStatusAndDepartureTimeBetween(final String status, final LocalDateTime start, final LocalDateTime end);
}
