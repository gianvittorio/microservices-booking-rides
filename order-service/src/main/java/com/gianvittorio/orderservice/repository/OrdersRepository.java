package com.gianvittorio.orderservice.repository;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<OrderEntity, Long> {
    Flux<OrderEntity> findByPassengerIdAndDriverId(final Long passengerId, final Long driverId);
}
