package com.gianvittorio.usersservice.unit.repository;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.repository.UsersRepository;
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
public class UsersRepositoryTest {

    @Autowired
    UsersRepository repository;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    @DisplayName("Must return user whenever corresponding id exists.")
    public void findUserByDocumentTest() {

        // Given
        final UserEntity userEntity = UserEntity.builder()
                .document("000.000.000-01")
                .firstname("Jane")
                .lastname("Doe")
                .phone("+5547900000000")
                .email("jane.doe@nowhere.net")
                .build();

        databaseClient.sql("INSERT INTO users (firstname, lastname, document, phone, email) VALUES (:firstname, :lastname, :document, :phone, :email)")
                .bind("firstname", userEntity.getFirstname())
                .bind("lastname", userEntity.getLastname())
                .bind("document", userEntity.getDocument())
                .bind("phone", userEntity.getPhone())
                .bind("email", userEntity.getEmail())
                .then()
                .block();

        // When and Then
        this.repository.findByDocument(userEntity.getDocument())
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getFirstname())
                            .isEqualTo(userEntity.getFirstname());
                    assertThat(entity.getLastname())
                            .isEqualTo(userEntity.getLastname());
                    assertThat(entity.getPhone())
                            .isEqualTo(userEntity.getPhone());
                    assertThat(entity.getEmail())
                            .isEqualTo(userEntity.getEmail());
                })
                .verifyComplete();
    }
}
