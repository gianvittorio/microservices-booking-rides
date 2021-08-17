package com.gianvittorio.common.web.dto.drivers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverRequestDTO {

    private String firstname;

    private String lastname;

    private String document;

    private String phone;

    private String email;

    private String category;

    private String location;

    private Boolean isAvailable;

    private Integer rating;
}
