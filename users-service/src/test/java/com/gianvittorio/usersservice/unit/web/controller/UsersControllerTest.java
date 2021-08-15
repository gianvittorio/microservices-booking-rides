package com.gianvittorio.usersservice.unit.web.controller;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.service.UsersService;
import com.gianvittorio.usersservice.web.controller.UsersController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
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
    public void givenValidRequestthenReturnValidResponse() {

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
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
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
}
