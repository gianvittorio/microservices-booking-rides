package com.gianvittorio.orderservice.service;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import reactor.core.publisher.Mono;

public interface DriversService {

    Mono<DriverResponseDTO> findAvailableDriver(final DriverRequestDTO driverRequestDTO);
}
