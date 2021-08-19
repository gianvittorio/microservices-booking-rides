package com.gianvittorio.orderservice.web.controller;

import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.service.DriversService;
import com.gianvittorio.orderservice.service.OrdersService;
import com.gianvittorio.orderservice.service.UsersService;
import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import com.gianvittorio.orderservice.web.dto.OrderResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    private final UsersService usersService;

    private final DriversService driversService;

    @GetMapping(path = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OrderResponseDTO>> findOrderById(@PathVariable("id") Long id) {
        return ordersService.findById(id)
                .map(orderEntity -> {
                    final OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
                    BeanUtils.copyProperties(orderEntity, orderResponseDTO);

                    return orderResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order set for creation"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<OrderResponseDTO>> createOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {


        final Mono<OrderEntity> orderEntityMono = Mono.just(orderRequestDTO)
                .flatMap(this::aggregate)
                .onErrorMap(error -> {
                    log.error(error);

                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error);
                })
                .doOnError(log::error);

        final Mono<OrderResponseDTO> orderResponseMono = orderEntityMono.map(
                orderEntity -> {
                    final OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                            .id(orderEntity.getId())
                            .departureTime(orderEntity.getDepartureTime())
                            .status(orderEntity.getStatus())
                            .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                            .build();

                    return orderResponseDTO;
                });

        return orderResponseMono.map(ResponseEntity::ok);
    }

    private Mono<OrderEntity> aggregate(final OrderRequestDTO orderRequestDTO) {
        return usersService.findUserByDocument(orderRequestDTO.getDocument())
                .flatMap(user -> {
                    final Mono<DriverResponseDTO> driverMono = driversService.findAvailableDriver(orderRequestDTO.getCategory(), orderRequestDTO.getOrigin(), user.getRating());

                    return driverMono.flatMap(driver -> {
                        final OrderEntity orderEntity = OrderEntity.builder()
                                .passengerId(user.getId())
                                .driverId(driver.getId())
                                .origin(orderRequestDTO.getOrigin())
                                .destination(orderRequestDTO.getDestination())
                                .departureTime(orderRequestDTO.getDeparture())
                                .status("pending")
                                .build();

                        return ordersService.save(orderEntity);
                    });
                });
    }
}
