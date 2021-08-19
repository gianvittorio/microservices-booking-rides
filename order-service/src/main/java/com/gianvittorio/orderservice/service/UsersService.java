package com.gianvittorio.orderservice.service;

import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import reactor.core.publisher.Mono;

public interface UsersService {

    Mono<UserResponseDTO> findUserById(final Long id);

    Mono<UserResponseDTO> findUserByDocument(final String document);
}
