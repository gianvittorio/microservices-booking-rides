package com.gianvittorio.orderservice.unit.repository;

import com.gianvittorio.orderservice.repository.OrdersRepository;
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
public class OrdersRepositoryTest {

    @Autowired
    OrdersRepository repository;

    @Autowired
    DatabaseClient databaseClient;

    @Test
    @DisplayName("Persist order entity.")
    public void createOrderTest() {

        // Given

        // When

        // Then
        this.repository.findByPassengerIdAndDriverId(123l, 321l)
                .take(1)
                .as(StepVerifier::create)
                .consumeNextWith(entity -> {
                    assertThat(entity.getOrigin())
                            .isEqualTo("X");
                    assertThat(entity.getDestination())
                            .isEqualTo("Y");
                })
                .verifyComplete();
    }
}
