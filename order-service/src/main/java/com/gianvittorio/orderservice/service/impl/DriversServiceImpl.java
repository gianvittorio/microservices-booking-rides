package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.exceptions.DriverNotFoundException;
import com.gianvittorio.common.exceptions.NetworkException;
import com.gianvittorio.common.exceptions.ServiceException;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.service.DriversService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@Log4j2
@RequiredArgsConstructor
public class DriversServiceImpl implements DriversService {

    private final WebClient webClient;

    @Value("${app.driver-matching-service.path}")
    private String path;

    @Override
    public Mono<DriverResponseDTO> findById(final Long id) {

        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/id/").concat(String.valueOf(id)))
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new DriverNotFoundException("Driver was not found", HttpStatus.NOT_FOUND.value())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new NetworkException(String.valueOf(response.rawStatusCode()))))
                .bodyToMono(DriverResponseDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(NetworkException.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .doOnError(log::error);
    }

    @Override
    public Mono<DriverResponseDTO> findByDocument(final String document) {

        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/document/").concat(document))
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new DriverNotFoundException("Driver was not found", HttpStatus.NOT_FOUND.value())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new NetworkException(String.valueOf(response.rawStatusCode()))))
                .bodyToMono(DriverResponseDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(NetworkException.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .doOnError(log::error);
    }

    @Override
    public Mono<DriverResponseDTO> findAvailableDriver(final String category, final String location, final Integer rating) {
        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/search"))
                .queryParam("category", category)
                .queryParam("location", location)
                .queryParam("rating", rating)
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new DriverNotFoundException("Driver was not found", HttpStatus.NOT_FOUND.value())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new NetworkException(String.valueOf(response.rawStatusCode()))))
                .bodyToMono(DriverResponseDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(NetworkException.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }))
                .doOnError(log::error);
    }
}
