package com.gianvittorio.usersservice.unit.web.controller;

import com.gianvittorio.common.web.dto.users.UserRequestDTO;
import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.service.UsersService;
import com.gianvittorio.usersservice.web.controller.UsersController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = UsersController.class)
@TestPropertySource(properties = "server.port=8081")
public class UsersControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    UsersService usersService;

    @Test
    @DisplayName("Must return user whenever searched by respective document")
    public void findUserByDocumentTest() {

        // Given
        final String document = "000.000.000-00";

        final UserEntity userEntity = UserEntity.builder()
                .firstname("John")
                .lastname("Doe")
                .document(document)
                .phone("'+551190000000")
                .email("john.doe@somewhere.net")
                .build();

        BDDMockito.given(usersService.findUserByDocument(document))
                .willReturn(Mono.just(userEntity));

        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("users/".concat(document))
                .build()
                .toUriString();

        webTestClient.get()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.firstname").isEqualTo(userEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(userEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(userEntity.getPhone())
                .jsonPath("$.email").isEqualTo(userEntity.getEmail());

        verify(usersService)
                .findUserByDocument(document);
    }

    @Test
    @DisplayName("Must create user.")
    public void createUserTest() {

        // Given
        final UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .document("000.000.000-00")
                .phone("'+551190000000")
                .email("john.doe@somewhere.net")
                .build();

        final UserEntity userEntity = new UserEntity();
        final long id = 123l;
        userEntity.setId(id);
        BeanUtils.copyProperties(userRequestDTO, userEntity);

        given(usersService.saveUser(any(UserEntity.class)))
                .willReturn(Mono.just(userEntity));

        // When
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("users")
                .build()
                .toUriString();

        // When and Then
        webTestClient.post()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(Mono.just(userRequestDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(userEntity.getId())
                .jsonPath("$.firstname").isEqualTo(userEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(userEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(userEntity.getPhone())
                .jsonPath("$.email").isEqualTo(userEntity.getEmail());

        verify(usersService)
                .saveUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Must update user.")
    public void updateUserTest() {

        // Given
        final UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .document("000.000.000-00")
                .phone("'+551190000000")
                .email("john.doe@somewhere.net")
                .build();

        final UserEntity userEntity = new UserEntity();
        final long id = 123l;
        userEntity.setId(id);
        BeanUtils.copyProperties(userRequestDTO, userEntity);

        given(usersService.findUserByDocument(userRequestDTO.getDocument()))
                .willReturn(Mono.just(userEntity));
        given(usersService.saveUser(any(UserEntity.class)))
                .willReturn(Mono.just(userEntity));

        // When
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("users/".concat(userRequestDTO.getDocument()))
                .build()
                .toUriString();

        // When and Then
        webTestClient.put()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(Mono.just(userRequestDTO), UserRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(userEntity.getId())
                .jsonPath("$.firstname").isEqualTo(userEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(userEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(userEntity.getPhone())
                .jsonPath("$.email").isEqualTo(userEntity.getEmail());

        verify(usersService)
                .findUserByDocument(any(String.class));
        verify(usersService)
                .saveUser(any(UserEntity.class));
    }

    @Test
    @DisplayName("Must delete user.")
    public void deleteUserTest() {

        // Given
        final String document = "000.000.000-00";

        BDDMockito.given(usersService.findUserByDocument(document))
                .willReturn(Mono.empty());

        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("users/".concat(document))
                .build()
                .toUriString();

        webTestClient.delete()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .exchange()
                .expectStatus().isNoContent();

        verify(usersService)
                .deleteUserByDocument(document);
    }
}
