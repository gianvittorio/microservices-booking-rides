package com.gianvittorio.usersservice.repository;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UsersRepository extends ReactiveCrudRepository<UserEntity, Long> {

    Flux<UserEntity> findByDocument(final String document);

    @Modifying
    @Query("DELETE FROM users WHERE document = :document")
    Mono<Void> deleteByDocument(final String document);
}
