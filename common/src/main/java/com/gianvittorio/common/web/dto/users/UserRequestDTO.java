package com.gianvittorio.common.web.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String document;

    @NotNull
    private String phone;

    @NotNull
    private String email;
}
