package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.service.UsersService;
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
public class UsersServiceImpl implements UsersService {

    private final WebClient webClient;

    @Value("${app.users-service.path}")
    private String path;

    @Override
    public Mono<UserResponseDTO> findUserByDocument(final String document) {

        final String uriString = UriComponentsBuilder.newInstance()
                .path(path.concat("/").concat(document))
                .build()
                .toUriString();

        return webClient.get()
                .uri(uriString)
                .retrieve()
                .bodyToMono(UserResponseDTO.class);
    }
}
