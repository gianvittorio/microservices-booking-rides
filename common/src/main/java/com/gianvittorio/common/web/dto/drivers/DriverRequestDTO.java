package com.gianvittorio.common.web.dto.drivers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverRequestDTO {

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

    @NotNull
    private String category;

    @NotNull
    private String location;

    @NotNull
    private Boolean isAvailable;

    @NotNull
    private Integer rating;
}
