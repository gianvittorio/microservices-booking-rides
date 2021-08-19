package com.gianvittorio.usersservice.web.controller;

import com.gianvittorio.common.web.dto.users.UserRequestDTO;
import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @GetMapping(path = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Mono<ResponseEntity<UserResponseDTO>> findUserById(@PathVariable("id") final Long id) {
        return usersService.findById(id)
                .map(userEntity -> {
                    final UserResponseDTO userResponseDTO = new UserResponseDTO();
                    BeanUtils.copyProperties(userEntity, userResponseDTO);

                    return userResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @GetMapping(path = "/document/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find user by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Mono<ResponseEntity<UserResponseDTO>> findUserByDocument(@PathVariable("document") final String document) {
        return usersService.findUserByDocument(document)
                .map(userEntity -> {
                    final UserResponseDTO userResponseDTO = new UserResponseDTO();
                    BeanUtils.copyProperties(userEntity, userResponseDTO);

                    return userResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User set for creation"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<UserResponseDTO>> createUser(@Valid @RequestBody final UserRequestDTO userRequestDTO) {
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
                .map(ResponseEntity::ok)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @PutMapping(path = "/{document}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User set for creation"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
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
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @DeleteMapping(path = "/id/{id}")
    @Operation(summary = "Delete user by Id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserById(@PathVariable("id") final Long id) {
        return usersService.deleteUserById(id);
    }

    @DeleteMapping(path = "/document/{document}")
    @Operation(summary = "Delete user by document")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteUserByDocument(@PathVariable("document") final String document) {
        return usersService.deleteUserByDocument(document);
    }
}
