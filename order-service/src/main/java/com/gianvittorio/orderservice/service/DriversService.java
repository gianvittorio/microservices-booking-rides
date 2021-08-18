package com.gianvittorio.orderservice.service;

import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import reactor.core.publisher.Mono;

public interface DriversService {

    Mono<DriverResponseDTO> findAvailableDriver(final String category, final String location, final Integer rating);
}
