package com.gianvittorio.orderservice.service;

import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import reactor.core.publisher.Mono;

public interface DriversService {

    Mono<DriverResponseDTO> findById(final Long id);

    Mono<DriverResponseDTO> findByDocument(final String document);

    Mono<DriverResponseDTO> findAvailableDriver(final String category, final String location, final Integer rating);
}
