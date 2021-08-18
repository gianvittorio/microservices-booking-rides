package com.gianvittorio.orderservice.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime departureTime;

    private String driverName;

    private String status;
}
