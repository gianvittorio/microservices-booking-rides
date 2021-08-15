package com.gianvittorio.usersservice.web.controller;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.service.UsersService;
import com.gianvittorio.usersservice.web.dto.UserRequestDTO;
import com.gianvittorio.usersservice.web.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping(path = "/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserResponseDTO>> findUserByDocument(@PathVariable("document") final String document) {
        return usersService.findUserByDocument(document)
                .map(userEntity -> {
                    final UserResponseDTO userResponseDTO = new UserResponseDTO();
                    BeanUtils.copyProperties(userEntity, userResponseDTO);

                    return userResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserResponseDTO>> createUser(@RequestBody final UserRequestDTO userRequestDTO) {
        return Mono.just(userRequestDTO)
                .map(requestDTO -> {
                    final UserEntity userEntity = new UserEntity();
                    BeanUtils.copyProperties(requestDTO, userEntity);

                    return userEntity;
                })
                .flatMap(usersService::saveUser)
                .map(userEntity -> {
                    final UserResponseDTO userResponseDTO = new UserResponseDTO();
                    BeanUtils.copyProperties(userEntity, userResponseDTO);

                    return userResponseDTO;
                })
                .map(ResponseEntity::ok);
    }

    @PutMapping(path = "/{document}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserResponseDTO>> updateUser(@PathVariable("document") final String document, @RequestBody final UserRequestDTO userRequestDTO) {

        return usersService.findUserByDocument(document)
                .flatMap(userEntity -> {
                    BeanUtils.copyProperties(userRequestDTO, userEntity);

                    return usersService.saveUser(userEntity);
                })
                .map(userEntity -> {
                    final UserResponseDTO userResponseDTO = new UserResponseDTO();
                    BeanUtils.copyProperties(userEntity, userResponseDTO);

                    return userResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{document}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserByDocument(@PathVariable("document") final String document) {
        return usersService.deleteUserByDocument(document);
    }
}
