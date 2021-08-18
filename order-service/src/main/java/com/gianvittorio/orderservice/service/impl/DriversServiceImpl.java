package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.service.DriversService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class DriversServiceImpl implements DriversService {

    private final WebClient webClient;

    @Value("${driver-matching-service}")
    private String hostname;

    @Override
    public Mono<DriverResponseDTO> findAvailableDriver(final DriverRequestDTO driverRequestDTO) {
        return null;
    }
}
