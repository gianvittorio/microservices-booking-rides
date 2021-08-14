package com.gianvittorio.orderservice.repository;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends ReactiveCrudRepository<OrderEntity, Long> {

}
