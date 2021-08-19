package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.exceptions.NetworkException;
import com.gianvittorio.orderservice.exceptions.ServiceException;
import com.gianvittorio.orderservice.exceptions.UserNotFoundException;
import com.gianvittorio.orderservice.service.UsersService;
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
public class UsersServiceImpl implements UsersService {

    private final WebClient webClient;

    @Value("${app.users-service.path}")
    private String path;

    @Override
    public Mono<UserResponseDTO> findUserById(Long id) {
        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/id/").concat(String.valueOf(id)))
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException("User was not found", HttpStatus.NOT_FOUND.value())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new NetworkException(String.valueOf(response.rawStatusCode()))))
                .bodyToMono(UserResponseDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(NetworkException.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }));
    }

    @Override
    public Mono<UserResponseDTO> findUserByDocument(final String document) {

        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/document/").concat(document))
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.error(new UserNotFoundException("User was not found", HttpStatus.NOT_FOUND.value())))
                .onStatus(HttpStatus::is5xxServerError, response -> Mono.error(new NetworkException(String.valueOf(response.rawStatusCode()))))
                .bodyToMono(UserResponseDTO.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(2))
                        .filter(NetworkException.class::isInstance)
                        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                            throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                        }));
    }
}
