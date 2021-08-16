package com.gianvittorio.drivermatchingservice.service;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import reactor.core.publisher.Mono;

public interface DriversService {

    Mono<DriverEntity> findFirstAvailableByCategory(final String category);

    Mono<DriverEntity> findByDocument(final String document);

    Mono<DriverEntity> save(final DriverEntity driverEntity);

    Mono<Void> deleteByDocument(final String document);
}