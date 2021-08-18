package com.gianvittorio.orderservice.web.controller;

import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.service.DriversService;
import com.gianvittorio.orderservice.service.OrdersService;
import com.gianvittorio.orderservice.service.UsersService;
import com.gianvittorio.orderservice.web.dto.OrderRequestDTO;
import com.gianvittorio.orderservice.web.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/orders")
@Log4j2
@RequiredArgsConstructor
public class OrdersController {

    private final OrdersService ordersService;

    private final UsersService usersService;

    private final DriversService driversService;

    @GetMapping(path = "/users/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<UserResponseDTO> findUserByDocument(@PathVariable("document") final String document) {

        return usersService.findUserByDocument(document);
    }

    @GetMapping(path = "/drivers/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DriverResponseDTO>> findFirstAvailableDriver(@RequestParam("category") final String category,
                                                                            @RequestParam("location") final String location,
                                                                            @RequestParam("rating") final Integer rating) {
        return driversService.findAvailableDriver(category, location, rating)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OrderResponseDTO>> createOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {

        final Mono<OrderEntity> orderEntityMono = usersService.findUserByDocument(orderRequestDTO.getDocument())
                .flatMap(user -> {

                    log.info("user: {}; {}; {}, {}", user.getRating(), orderRequestDTO.getDocument(), orderRequestDTO.getDeparture(), orderRequestDTO.getOrigin());

                    final Mono<DriverResponseDTO> driverMono = driversService.findAvailableDriver(orderRequestDTO.getCategory(), orderRequestDTO.getOrigin(), user.getRating())
                            .map(driver -> {
                                log.info("driver: {}; {}; {}; {}", driver.getFirstname(), driver.getCategory(), driver.getLocation(), driver.getRating());

                                return driver;
                            });

                    return driverMono.flatMap(driver -> {
                        final OrderEntity orderEntity = OrderEntity.builder()
                                .passengerId(user.getId())
                                .driverId(driver.getId())
                                .origin(orderRequestDTO.getOrigin())
                                .destination(orderRequestDTO.getDestination())
                                .departureTime(orderRequestDTO.getDeparture())
                                .status("pending")
                                .build();

                        return ordersService.createOrder(orderEntity);
                    });
                });

        final Mono<OrderResponseDTO> orderResponseMono = orderEntityMono.map(
                orderEntity -> {
                    final OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                            .id(orderEntity.getId())
                            .departureTime(orderEntity.getDepartureTime())
                            .status(orderEntity.getStatus())
                            .createdAt(LocalDateTime.now())
                            .build();

                    return orderResponseDTO;
                });

        return orderResponseMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
