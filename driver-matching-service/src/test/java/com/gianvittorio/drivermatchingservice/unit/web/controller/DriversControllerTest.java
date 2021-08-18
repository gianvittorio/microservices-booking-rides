package com.gianvittorio.drivermatchingservice.unit.web.controller;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import com.gianvittorio.drivermatchingservice.service.DriversService;
import com.gianvittorio.drivermatchingservice.web.controller.DriversController;
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
@WebFluxTest(controllers = DriversController.class)
@TestPropertySource(properties = "server.port=8081")
public class DriversControllerTest {

    @Autowired
    WebTestClient webTestClient;

    @MockBean
    DriversService driversService;

    @Test
    @DisplayName("Must return driver whenever searched by respective document")
    public void findDriverByDocumentTest() {

        // Given
        final String document = "000.000.000-00";

        final DriverEntity driverEntity = DriverEntity.builder()
                .document(document)
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .rating(0)
                .isAvailable(true)
                .build();

        given(driversService.findByDocument(document))
                .willReturn(Mono.just(driverEntity));

        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("drivers/".concat(document))
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
                .jsonPath("$.firstname").isEqualTo(driverEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(driverEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(driverEntity.getPhone())
                .jsonPath("$.email").isEqualTo(driverEntity.getEmail())
                .jsonPath("$.category").isEqualTo(driverEntity.getCategory())
                .jsonPath("$.location").isEqualTo(driverEntity.getLocation());

        verify(driversService)
                .findByDocument(document);
    }

    @Test
    @DisplayName("Must return driver whenever searched by respective document")
    public void findFirstAvailableDriverTest() {

        // Given
        final String category = "luxury";
        final String location = "X";
        final Integer rating = 5;

        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-00")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category(category)
                .location(location)
                .isAvailable(true)
                .rating(rating)
                .build();

        given(driversService.findFirstAvailable(category, location, rating))
                .willReturn(Mono.just(driverEntity));

        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("drivers".concat("/search"))
                .queryParam("category", category)
                .queryParam("location", location)
                .queryParam("rating", rating)
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
                .jsonPath("$.firstname").isEqualTo(driverEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(driverEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(driverEntity.getPhone())
                .jsonPath("$.email").isEqualTo(driverEntity.getEmail())
                .jsonPath("$.category").isEqualTo(driverEntity.getCategory())
                .jsonPath("$.location").isEqualTo(driverEntity.getLocation());

        verify(driversService)
                .findFirstAvailable(category, location, rating);
    }

    @Test
    @DisplayName("Must create driver.")
    public void createDriverTest() {

        // Given
        final DriverRequestDTO driverRequestDTO = DriverRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .document("000.000.000-00")
                .phone("'+551190000000")
                .email("john.doe@somewhere.net")
                .category("luxury")
                .location("X")
                .rating(4)
                .build();

        final DriverEntity driverEntity = new DriverEntity();
        final long id = 123l;
        driverEntity.setId(id);
        BeanUtils.copyProperties(driverRequestDTO, driverEntity);

        given(driversService.save(any(DriverEntity.class)))
                .willReturn(Mono.just(driverEntity));

        // When
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("drivers")
                .build()
                .toUriString();

        // When and Then
        webTestClient.post()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(Mono.just(driverRequestDTO), DriverRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(driverEntity.getId())
                .jsonPath("$.firstname").isEqualTo(driverEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(driverEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(driverEntity.getPhone())
                .jsonPath("$.email").isEqualTo(driverEntity.getEmail())
                .jsonPath("$.category").isEqualTo(driverEntity.getCategory())
                .jsonPath("$.location").isEqualTo(driverEntity.getLocation())
                .jsonPath("$.rating").isEqualTo(driverEntity.getRating());

        verify(driversService)
                .save(any(DriverEntity.class));
    }

    @Test
    @DisplayName("Must update driver.")
    public void updateDriverTest() {

        // Given
        final DriverRequestDTO driverRequestDTO = DriverRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .document("000.000.000-00")
                .phone("'+551190000000")
                .email("john.doe@somewhere.net")
                .category("luxury")
                .location("X")
                .rating(4)
                .build();

        final DriverEntity driverEntity = new DriverEntity();
        final long id = 123l;
        driverEntity.setId(id);
        BeanUtils.copyProperties(driverRequestDTO, driverEntity);

        given(driversService.findByDocument(driverRequestDTO.getDocument()))
                .willReturn(Mono.just(driverEntity));
        given(driversService.save(any(DriverEntity.class)))
                .willReturn(Mono.just(driverEntity));

        // When
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("drivers/".concat(driverRequestDTO.getDocument()))
                .build()
                .toUriString();

        // When and Then
        webTestClient.put()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .body(Mono.just(driverRequestDTO), DriverRequestDTO.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(driverEntity.getId())
                .jsonPath("$.firstname").isEqualTo(driverEntity.getFirstname())
                .jsonPath("$.lastname").isEqualTo(driverEntity.getLastname())
                .jsonPath("$.phone").isEqualTo(driverEntity.getPhone())
                .jsonPath("$.email").isEqualTo(driverEntity.getEmail())
                .jsonPath("$.category").isEqualTo(driverEntity.getCategory())
                .jsonPath("$.location").isEqualTo(driverEntity.getLocation())
                .jsonPath("$.rating").isEqualTo(driverEntity.getRating());

        verify(driversService)
                .findByDocument(any(String.class));
        verify(driversService)
                .save(any(DriverEntity.class));
    }

    @Test
    @DisplayName("Must delete driver.")
    public void deleteDriverTest() {

        // Given
        final String document = "000.000.000-00";

        BDDMockito.given(driversService.findByDocument(document))
                .willReturn(Mono.empty());

        // When and Then
        final String uriString = UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("localhost")
                .port("8081")
                .path("drivers/".concat(document))
                .build()
                .toUriString();

        webTestClient.delete()
                .uri(uriString)
                .headers(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                })
                .exchange()
                .expectStatus().isNoContent();

        verify(driversService)
                .deleteByDocument(document);
    }
}
