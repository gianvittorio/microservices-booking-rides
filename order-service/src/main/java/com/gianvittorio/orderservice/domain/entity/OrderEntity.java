package com.gianvittorio.orderservice.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    private Long id;

    private Long passengerId;

    private Long driverId;

    private String origin;

    private String destination;

    private ZonedDateTime departureTime;
}
