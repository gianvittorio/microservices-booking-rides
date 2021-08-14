package com.gianvittorio.orderservice.domain.entity;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String rideId;

    @Column
    @NotNull
    @NotBlank
    private String passengerId;

    @Column
    @NotNull
    @NotBlank
    private String driverId;

    @Column
    @NotNull
    @NotBlank
    private String origin;

    @Column
    @NotNull
    @NotBlank
    private String destination;

    @Column
    @NotNull
    private ZonedDateTime departureTime;
}
