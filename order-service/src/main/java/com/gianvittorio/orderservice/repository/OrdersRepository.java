package com.gianvittorio.orderservice.repository;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrdersRepository extends ReactiveCrudRepository<OrderEntity, String> {

}
