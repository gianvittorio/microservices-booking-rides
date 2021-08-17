package com.gianvittorio.drivermatchingservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponseDTO {

    private Long id;

    private String firstname;

    private String lastname;

    private String document;

    private String phone;

    private String email;

    private String category;

    private String location;

    private Integer rating;
}
