package com.gianvittorio.drivermatchingservice.repository;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DriversRepository extends ReactiveCrudRepository<DriverEntity, Long> {

    @Query("SELECT * FROM drivers WHERE is_available = true LIMIT 1")
    Mono<DriverEntity> findFirstAvailable();
}
