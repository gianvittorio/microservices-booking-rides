package com.gianvittorio.usersservice.service.impl;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.repository.UsersRepository;
import com.gianvittorio.usersservice.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final UsersRepository repository;

    @Override
    public Mono<UserEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Mono<UserEntity> findUserByDocument(final String document) {
        return repository.findByDocument(document).next();
    }

    @Override
    public Mono<UserEntity> saveUser(final UserEntity user) {
        return repository.save(user);
    }

    @Override
    public Mono<Void> deleteUserByDocument(final String document) {
        return repository.deleteByDocument(document);
    }
}
