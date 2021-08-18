package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final WebClient webClient;

    @Value("{app.user-service.path}")
    private String path;

    @Override
    public Mono<UserResponseDTO> findUserByDocument(final String document) {

        return webClient.get()
                .uri(path.concat("/").concat(document))
                .retrieve()
                .bodyToMono(UserResponseDTO.class);
    }
}
