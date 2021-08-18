package com.gianvittorio.orderservice.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    @JsonProperty("document")
    private String document;

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("destination")
    private String destination;

    @JsonProperty("category")
    private String category;

    @JsonProperty("is_booked")
    private Boolean isBooked;

    @JsonProperty("departure_time")
    private LocalDateTime departureTime;
}
