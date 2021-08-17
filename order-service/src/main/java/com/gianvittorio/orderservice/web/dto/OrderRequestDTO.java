package com.gianvittorio.orderservice.web.dto;

import com.gianvittorio.common.domain.Category;
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

    private String document;

    private String origin;

    private String destination;

    private Category category;

    private Boolean isBooked;

    private LocalDateTime departureTime;
}
