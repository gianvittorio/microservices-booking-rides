package com.gianvittorio.orderservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    private Long orderId;

    private String origin;

    private String destination;

    private String userEmail;

    private String driverName;

    private LocalDateTime departure;

    private LocalDateTime createdAt;
}
