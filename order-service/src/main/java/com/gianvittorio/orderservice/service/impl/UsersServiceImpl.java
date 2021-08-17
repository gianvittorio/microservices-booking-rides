package com.gianvittorio.orderservice.service.impl;

import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.service.UsersService;
import reactor.core.publisher.Mono;

public class UsersServiceImpl implements UsersService {
    @Override
    public Mono<UserResponseDTO> findUserByDocument(String document) {
        return null;
    }
}
