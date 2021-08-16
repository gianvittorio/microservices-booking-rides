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
                .isAvailable(true)
                .build();

        // When and Then
        databaseClient.sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, is_available) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :is_available)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("is_available", driverEntity.getIsAvailable())
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
                    assertThat(entity.getIsAvailable())
                            .isEqualTo(driverEntity.getIsAvailable());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Must return driver whenever corresponding id exists.")
    public void findFirstAvailableDriverByCategoryTest() {

        // Given
        final DriverEntity driverEntity = DriverEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .category("luxury")
                .isAvailable(true)
                .build();

        // When and Then
        databaseClient.sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, is_available) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :is_available)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("is_available", driverEntity.getIsAvailable())
                .then()
                .thenMany(this.driversRepository.findFirstAvailableByCategory(driverEntity.getCategory()))
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
                .isAvailable(true)
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
                .isAvailable(true)
                .build();

        final String lastname = "Smith";

        // When and Then
        databaseClient.sql("INSERT INTO drivers (firstname, lastname, document, phone, email, category, is_available) VALUES (:firstname, :lastname, :document, :phone, :email, :category, :is_available)")
                .bind("firstname", driverEntity.getFirstname())
                .bind("lastname", driverEntity.getLastname())
                .bind("document", driverEntity.getDocument())
                .bind("phone", driverEntity.getPhone())
                .bind("email", driverEntity.getEmail())
                .bind("category", driverEntity.getCategory())
                .bind("is_available", driverEntity.getIsAvailable())
                .then()
                .thenMany(this.driversRepository.findByDocument(driverEntity.getDocument()))
                .take(1)
                .flatMap(driver -> {
                    driver.setLastname(lastname);
                    return driversRepository.save(driver);
                })
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getLastname())
                            .isEqualTo(lastname);
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
