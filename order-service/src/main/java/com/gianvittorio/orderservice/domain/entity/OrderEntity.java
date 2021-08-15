package com.gianvittorio.orderservice.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orders")
public class OrderEntity {

    @Id
    @Column("id")
    private Long id;

    @Column("passenger_id")
    private Long passengerId;

    @Column("driver_id")
    private Long driverId;

    @Column("origin")
    private String origin;

    @Column("destination")
    private String destination;

    @Column("departure_time")
    private ZonedDateTime departureTime;
}
