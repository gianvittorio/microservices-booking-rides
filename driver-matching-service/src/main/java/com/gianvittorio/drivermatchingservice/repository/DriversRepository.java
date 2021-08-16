package com.gianvittorio.drivermatchingservice.repository;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DriversRepository extends ReactiveCrudRepository<DriverEntity, Long> {

   Flux<DriverEntity> findByDocument(final String document);

   @Modifying
   @Query("DELETE FROM drivers WHERE document = :document")
   Mono<Void> deleteByDocument(final String document);

   @Query("SELECT * FROM drivers WHERE is_available = true AND category = :category LIMIT 1")
   Mono<DriverEntity> findFirstAvailableByCategory(final String category);
}
