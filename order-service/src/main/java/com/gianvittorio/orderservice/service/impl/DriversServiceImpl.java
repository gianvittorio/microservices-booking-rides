package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.service.DriversService;
import reactor.core.publisher.Mono;

public class DriversServiceImpl implements DriversService {
    @Override
    public Mono<DriverResponseDTO> findAvailableDriver(final DriverRequestDTO driverRequestDTO) {
        return null;
    }
}
