package com.gianvittorio.orderservice.web.controller;

import com.gianvittorio.common.exceptions.ServiceException;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.orderservice.domain.OrderStatus;
import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.service.DriversService;
import com.gianvittorio.orderservice.service.OrdersService;
import com.gianvittorio.orderservice.service.UsersService;
import com.gianvittorio.orderservice.web.dto.OrderCreateRequestDTO;
import com.gianvittorio.orderservice.web.dto.OrderResponseDTO;
import com.gianvittorio.orderservice.web.dto.OrderUpdateRequestDTO;
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
import reactor.core.publisher.Mono;

import javax.validation.Valid;
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
    @Operation(summary = "Find order by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
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
    @Operation(summary = "Create order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order set for creation"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<OrderResponseDTO>> createOrder(@RequestBody @Valid final OrderCreateRequestDTO orderCreateRequestDTO) {

        final Mono<OrderEntity> orderEntityMono = Mono.just(orderCreateRequestDTO)
                .onErrorResume(error -> {
                    log.error(error);

                    return Mono.error(error);
                })
                .then(usersService.findUserByDocument(orderCreateRequestDTO.getDocument()))
                .flatMap(user -> {
                    final Mono<DriverResponseDTO> driverMono = driversService.findAvailableDriver(orderCreateRequestDTO.getCategory(), orderCreateRequestDTO.getOrigin(), user.getRating());

                    return driverMono.map(driver -> {
                        final OrderEntity orderEntity = OrderEntity.builder()
                                .passengerId(user.getId())
                                .driverId(driver.getId())
                                .origin(orderCreateRequestDTO.getOrigin())
                                .destination(orderCreateRequestDTO.getDestination())
                                .departure(orderCreateRequestDTO.getDeparture())
                                .status("pending")
                                .build();

                        return orderEntity;
                    });
                })
                .onErrorResume(error -> {
                    log.error(error);

                    return Mono.error(error);
                })
                .doOnError(log::error)
                .flatMap(ordersService::save);

        final Mono<OrderResponseDTO> orderResponseMono = orderEntityMono.map(
                orderEntity -> {
                    final OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                            .id(orderEntity.getId())
                            .departureTime(orderEntity.getDeparture())
                            .status(orderEntity.getStatus())
                            .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                            .build();

                    return orderResponseDTO;
                });

        return orderResponseMono.map(ResponseEntity::ok);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order set for creation"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<OrderResponseDTO>> updateOrder(@PathVariable("id") final Long id, @RequestBody @Valid final OrderUpdateRequestDTO orderUpdateRequestDTO) {

        final Mono<OrderEntity> orderEntityMono = Mono.just(orderUpdateRequestDTO)
                .onErrorResume(error -> {
                    log.error(error);

                    return Mono.error(error);
                })
                .then(ordersService.findById(id))
                .filter(orderEntity -> OrderStatus.PENDING.value().equals(orderEntity.getStatus()))
                .flatMap(orderEntity -> {
                    BeanUtils.copyProperties(orderUpdateRequestDTO, orderEntity);

                    return ordersService.save(orderEntity);
                });

        final Mono<OrderResponseDTO> orderResponseMono = orderEntityMono.map(
                orderEntity -> {
                    final OrderResponseDTO orderResponseDTO = OrderResponseDTO.builder()
                            .id(orderEntity.getId())
                            .departureTime(orderEntity.getDeparture())
                            .status(orderEntity.getStatus())
                            .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                            .build();

                    return orderResponseDTO;
                });

        return orderResponseMono.map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOrder(@PathVariable("id") final Long id) {
        return ordersService.findById(id)
                .filter(orderEntity -> OrderStatus.PENDING.value().equals(orderEntity.getStatus()))
                .map(OrderEntity::getId)
                .flatMap(ordersService::deleteById);
    }
}
