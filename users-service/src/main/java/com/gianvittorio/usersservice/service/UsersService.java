package com.gianvittorio.usersservice.service;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import reactor.core.publisher.Mono;

public interface UsersService {

    Mono<UserEntity> findUserByDocument(final String document);

    Mono<UserEntity> saveUser(final UserEntity user);

    Mono<Void> deleteUserByDocument(final String document);
}
