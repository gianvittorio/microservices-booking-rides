package com.gianvittorio.drivermatchingservice.repository;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface DriversRepository extends ReactiveCrudRepository<DriverEntity, Long> {

   Flux<DriverEntity> findByDocument(final String document);

   @Modifying
   @Query("DELETE FROM drivers WHERE document = :document")
   Mono<Void> deleteByDocument(final String document);

   @Query("SELECT * FROM drivers WHERE id IN (SELECT id FROM drivers WHERE is_available = true AND category = :category AND location = :location AND rating >= :rating LIMIT 1) OR " +
           "id IN (SELECT id FROM drivers WHERE is_available = true AND category = :category AND location = :location AND rating IN (SELECT MAX(rating) from drivers WHERE is_available = true AND category = :category AND location = :location AND rating < :rating)) " +
           "ORDER BY rating DESC LIMIT 1")
   Mono<DriverEntity> findFirstAvailable(final String category, final String location, final Integer rating);
}
