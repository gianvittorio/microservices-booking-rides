package com.gianvittorio.orderservice.domain.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
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
    @NotNull
    private Long passengerId;

    @Column("driver_id")
    @NotNull
    private Long driverId;

    @Column("origin")
    @NotNull
    private String origin;

    @Column("destination")
    @NotNull
    private String destination;

    @Column("departure_time")
    @NotNull
    private LocalDateTime departureTime;

    @Column("status")
    @NotNull
    private String status;
}
