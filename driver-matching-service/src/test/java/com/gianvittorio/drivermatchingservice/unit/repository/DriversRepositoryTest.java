package com.gianvittorio.drivermatchingservice.unit.repository;

import com.gianvittorio.drivermatchingservice.repository.DriversRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataR2dbcTest
public class DriversRepositoryTest {

    @Autowired
    DriversRepository driversRepository;

    @Autowired
    DatabaseClient databaseClient;

    @BeforeEach
    public void resetDB() {
//        databaseClient.sql("DELETE FROM users WHERE document = :document")
//                .bind("document", "000.000.000-01")
//                .then()
//                .block();
    }

    @Test
    @DisplayName("Must return user whenever corresponding id exists.")
    public void findUserByDocumentTest() {

        // Given
//        final UserEntity userEntity = UserEntity.builder()
//                .document("000.000.000-01")
//                .firstname("Jane")
//                .lastname("Doe")
//                .phone("+5547900000000")
//                .email("jane.doe@nowhere.net")
//                .build();
//
//        // When and Then
//        databaseClient.sql("INSERT INTO users (firstname, lastname, document, phone, email) VALUES (:firstname, :lastname, :document, :phone, :email)")
//                .bind("firstname", userEntity.getFirstname())
//                .bind("lastname", userEntity.getLastname())
//                .bind("document", userEntity.getDocument())
//                .bind("phone", userEntity.getPhone())
//                .bind("email", userEntity.getEmail())
//                .then()
//                .thenMany(this.repository.findByDocument(userEntity.getDocument()))
//                .take(1)
//                .as(StepVerifier::create)
//                .consumeNextWith(entity -> {
//                    assertThat(entity.getFirstname())
//                            .isEqualTo(userEntity.getFirstname());
//                    assertThat(entity.getLastname())
//                            .isEqualTo(userEntity.getLastname());
//                    assertThat(entity.getPhone())
//                            .isEqualTo(userEntity.getPhone());
//                    assertThat(entity.getEmail())
//                            .isEqualTo(userEntity.getEmail());
//                })
//                .verifyComplete();
    }

    @Test
    @DisplayName("Must create user.")
    public void createUserTest() {

        // Given
//        final UserEntity userEntity = UserEntity.builder()
//                .document("000.000.000-01")
//                .firstname("Jane")
//                .lastname("Doe")
//                .phone("+5547900000000")
//                .email("jane.doe@nowhere.net")
//                .build();
//
//        // When and Then
//        this.repository.save(userEntity)
//                .thenMany(this.repository.findByDocument(userEntity.getDocument()))
//                .take(1)
//                .as(StepVerifier::create)
//                .consumeNextWith(entity -> {
//                    assertThat(entity.getFirstname())
//                            .isEqualTo(userEntity.getFirstname());
//                    assertThat(entity.getLastname())
//                            .isEqualTo(userEntity.getLastname());
//                    assertThat(entity.getPhone())
//                            .isEqualTo(userEntity.getPhone());
//                    assertThat(entity.getEmail())
//                            .isEqualTo(userEntity.getEmail());
//                })
//                .verifyComplete();
    }

    @Test
    @DisplayName("Must update user.")
    public void updateUserTest() {

        // Given
//        final UserEntity userEntity = UserEntity.builder()
//                .document("000.000.000-01")
//                .firstname("Jane")
//                .lastname("Doe")
//                .phone("+5547900000000")
//                .email("jane.doe@nowhere.net")
//                .build();
//
//        final String lastname = "Smith";
//
//        // When and Then
//        databaseClient.sql("INSERT INTO users (firstname, lastname, document, phone, email) VALUES (:firstname, :lastname, :document, :phone, :email)")
//                .bind("firstname", userEntity.getFirstname())
//                .bind("lastname", userEntity.getLastname())
//                .bind("document", userEntity.getDocument())
//                .bind("phone", userEntity.getPhone())
//                .bind("email", userEntity.getEmail())
//                .then()
//                .thenMany(this.repository.findByDocument(userEntity.getDocument()))
//                .take(1)
//                .flatMap(user -> {
//                    user.setLastname(lastname);
//                    return repository.save(user);
//                })
//                .as(StepVerifier::create)
//                .consumeNextWith(entity -> {
//                    assertThat(entity.getLastname())
//                            .isEqualTo(lastname);
//                })
//                .verifyComplete();
    }

    @Test
    @DisplayName("Must delete user whenever corresponding id exists.")
    public void deleteUserByDocumentTest() {

//        // Given
//        final String document = "000.000.000-01";
//
//        // When and Then
//        this.repository.deleteByDocument(document)
//                .thenMany(this.repository.findByDocument(document))
//                        .as(StepVerifier::create)
//                        .expectComplete();
    }
}
