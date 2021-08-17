package com.gianvittorio.drivermatchingservice.unit.repository;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import com.gianvittorio.drivermatchingservice.repository.DriversRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataR2dbcTest
public class DriversRepositoryTest {

    @Autowired
    DriversRepository driversRepository;

    @Autowired
    DatabaseClient databaseClient;

    @BeforeEach
    public void resetDB() {
        databaseClient.sql("DELETE FROM drivers WHERE document = :document")
                .bind("document", "000.000.000-01")
                .then()
                .block();
    }

    @Test
    @DisplayName("Must return driver whenever corresponding id exists.")
    public void findDriverByDocumentTest() {

        // Given
        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .isAvailable(true)
                .rating(0)
                .build();

        // When and Then
        databaseClient
                .sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, location, is_available, rating) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :location, :is_available, :rating)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("location", driverEntity.getLocation())
                .bind("is_available", driverEntity.getIsAvailable())
                .bind("rating", driverEntity.getRating())
                .then()
                .thenMany(this.driversRepository.findByDocument(driverEntity.getDocument()))
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getId())
                            .isNotNull();
                    assertThat(entity.getFirstname())
                            .isEqualTo(driverEntity.getFirstname());
                    assertThat(entity.getLastname())
                            .isEqualTo(driverEntity.getLastname());
                    assertThat(entity.getPhone())
                            .isEqualTo(driverEntity.getPhone());
                    assertThat(entity.getEmail())
                            .isEqualTo(driverEntity.getEmail());
                    assertThat(entity.getCategory())
                            .isEqualTo(driverEntity.getCategory());
                    assertThat(entity.getLocation())
                            .isEqualTo(driverEntity.getLocation());
                    assertThat(entity.getIsAvailable())
                            .isEqualTo(driverEntity.getIsAvailable());
                    assertThat(entity.getRating())
                            .isEqualTo(driverEntity.getRating());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return very first driver matching location, category and  ceil rating")
    public void findFirstAvailableDriverWithCeilRating() {

        // Given
        final int rating = 5;

        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .rating(rating)
                .isAvailable(true)
                .build();

        // When and Then
        databaseClient
                .sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, location, is_available, rating) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :location, :is_available, :rating)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("location", driverEntity.getLocation())
                .bind("is_available", driverEntity.getIsAvailable())
                .bind("rating", driverEntity.getRating())
                .then()
                .thenMany(this.driversRepository.findFirstAvailable(driverEntity.getCategory(), driverEntity.getLocation(), driverEntity.getRating() - 1))
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getId())
                            .isNotNull();
                    assertThat(entity.getFirstname())
                            .isEqualTo(driverEntity.getFirstname());
                    assertThat(entity.getLastname())
                            .isEqualTo(driverEntity.getLastname());
                    assertThat(entity.getPhone())
                            .isEqualTo(driverEntity.getPhone());
                    assertThat(entity.getEmail())
                            .isEqualTo(driverEntity.getEmail());
                    assertThat(entity.getCategory())
                            .isEqualTo(driverEntity.getCategory());
                    assertThat(entity.getLocation())
                            .isEqualTo(driverEntity.getLocation());
                    assertThat(entity.getIsAvailable())
                            .isEqualTo(driverEntity.getIsAvailable());
                    assertThat((entity.getRating()))
                            .isGreaterThanOrEqualTo(driverEntity.getRating());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return very first driver matching location, category and strictly lesser rating")
    public void findFirstAvailableDriverWithLesserRating() {

        // Given
        final int rating = 5;

        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .rating(rating)
                .isAvailable(true)
                .build();

        // When and Then
        databaseClient
                .sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, location, is_available, rating) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :location, :is_available, :rating)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("location", driverEntity.getLocation())
                .bind("is_available", driverEntity.getIsAvailable())
                .bind("rating", driverEntity.getRating())
                .then()
                .thenMany(this.driversRepository.findFirstAvailable(driverEntity.getCategory(), driverEntity.getLocation(), driverEntity.getRating() + 1))
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getId())
                            .isNotNull();
                    assertThat(entity.getFirstname())
                            .isEqualTo(driverEntity.getFirstname());
                    assertThat(entity.getLastname())
                            .isEqualTo(driverEntity.getLastname());
                    assertThat(entity.getPhone())
                            .isEqualTo(driverEntity.getPhone());
                    assertThat(entity.getEmail())
                            .isEqualTo(driverEntity.getEmail());
                    assertThat(entity.getCategory())
                            .isEqualTo(driverEntity.getCategory());
                    assertThat(entity.getLocation())
                            .isEqualTo(driverEntity.getLocation());
                    assertThat(entity.getIsAvailable())
                            .isEqualTo(driverEntity.getIsAvailable());
                    assertThat((entity.getRating()))
                            .isGreaterThanOrEqualTo(driverEntity.getRating());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must create driver.")
    public void createDriverTest() {

        // Given
        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .isAvailable(true)
                .rating(0)
                .build();

        // When and Then
        this.driversRepository.save(driverEntity)
                .thenMany(this.driversRepository.findByDocument(driverEntity.getDocument()))
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getId())
                            .isNotNull();
                    assertThat(entity.getFirstname())
                            .isEqualTo(driverEntity.getFirstname());
                    assertThat(entity.getLastname())
                            .isEqualTo(driverEntity.getLastname());
                    assertThat(entity.getPhone())
                            .isEqualTo(driverEntity.getPhone());
                    assertThat(entity.getEmail())
                            .isEqualTo(driverEntity.getEmail());
                    assertThat(entity.getCategory())
                            .isEqualTo(driverEntity.getCategory());
                    assertThat(entity.getIsAvailable())
                            .isEqualTo(driverEntity.getIsAvailable());
                    assertThat(entity.getRating())
                            .isEqualTo(driverEntity.getRating());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must update driver.")
    public void updateDriverTest() {

        // Given
        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("comfort")
                .location("X")
                .rating(0)
                .isAvailable(true)
                .build();

        final Integer rating = 2;

        // When and Then
        databaseClient
                .sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, location, is_available, rating) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :location, :is_available, :rating)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("location", driverEntity.getLocation())
                .bind("is_available", driverEntity.getIsAvailable())
                .bind("rating", driverEntity.getRating())
                .then()
                .thenMany(this.driversRepository.findByDocument(driverEntity.getDocument()))
                .take(1)
                .flatMap(driver -> {
                    driver.setRating(rating);
                    return driversRepository.save(driver);
                })
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getRating())
                            .isEqualTo(rating);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must delete driver whenever corresponding id exists.")
    public void deleteDriverByDocumentTest() {

        // Given
        final String document = "000.000.000-01";

        // When and Then
        this.driversRepository.deleteByDocument(document)
                .thenMany(this.driversRepository.findByDocument(document))
                .as(StepVerifier::create)
                .expectComplete();
    }
}
