package com.gianvittorio.usersservice.unit.service;

import com.gianvittorio.usersservice.domain.entity.UserEntity;
import com.gianvittorio.usersservice.repository.UsersRepository;
import com.gianvittorio.usersservice.service.UsersService;
import com.gianvittorio.usersservice.service.impl.UsersServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UsersServiceImpl.class})
public class UsersServiceTest {

    @Autowired
    UsersService usersService;

    @MockBean
    UsersRepository usersRepository;

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


        // When

        // Then
    }
}
