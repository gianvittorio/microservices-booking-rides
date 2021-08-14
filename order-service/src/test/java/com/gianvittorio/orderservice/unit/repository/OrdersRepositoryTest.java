package com.gianvittorio.orderservice.unit.repository;

import com.gianvittorio.orderservice.repository.OrdersRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@DataR2dbcTest
public class OrdersRepositoryTest {

    @Autowired
    DatabaseClient client;

    @Autowired
    OrdersRepository repository;

    @Test
    @DisplayName("silly test")
    public void testDBClientExists() {
        assertThat(client)
                .isNotNull();
        assertThat(repository)
                .isNotNull();
    }
}
