package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.service.DriversService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class DriversServiceImpl implements DriversService {

    private final WebClient webClient;

    @Value("${app.driver-matching-service.path}")
    private String path;

    @Override
    public Mono<DriverResponseDTO> findAvailableDriver(final String category, final String location, final Integer rating) {
        final String uriString = UriComponentsBuilder.newInstance()
                .path(path)
                .queryParam("category", category)
                .queryParam("location", location)
                .queryParam("rating", rating)
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(DriverResponseDTO.class);
    }
}
