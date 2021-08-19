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
    public Mono<ResponseEntity<OrderResponseDTO>> createOrder(@RequestBody final OrderRequestDTO orderRequestDTO) {


        final Mono<OrderEntity> orderEntityMono = Mono.just(orderRequestDTO)
                .flatMap(this::getOrderEntityMono)
                .onErrorMap(error -> {
                    log.error(error);

                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error);
                })
                .doOnError(log::error)
                .flatMap(ordersService::save);

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

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order set for creation"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<OrderResponseDTO>> updateOrder(@PathVariable("id") final Long id, @RequestBody final OrderRequestDTO orderRequestDTO) {

        final Mono<OrderEntity> newOrderEntityMono = Mono.just(orderRequestDTO)
                .flatMap(this::getOrderEntityMono)
                .onErrorMap(error -> {
                    log.error(error);

                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error);
                })
                .doOnError(log::error)
                .filter(orderEntity -> orderEntity.getStatus().equals("pending"))
                .flatMap(newOrderEntity -> {
                    newOrderEntity.setId(id);
                    return ordersService.save(newOrderEntity);
                });

        final Mono<OrderResponseDTO> orderResponseMono = newOrderEntityMono.map(
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

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteOrder(@PathVariable("id") final Long id) {
        return ordersService.findById(id)
                .filter(orderEntity -> orderEntity.getStatus().equals("pending"))
                .map(OrderEntity::getId)
                .flatMap(ordersService::deleteById);
    }

    private Mono<OrderEntity> getOrderEntityMono(final OrderRequestDTO orderRequestDTO) {
        return usersService.findUserByDocument(orderRequestDTO.getDocument())
                .flatMap(user -> {
                    final Mono<DriverResponseDTO> driverMono = driversService.findAvailableDriver(orderRequestDTO.getCategory(), orderRequestDTO.getOrigin(), user.getRating());

                    return driverMono.map(driver -> {
                        final OrderEntity orderEntity = OrderEntity.builder()
                                .passengerId(user.getId())
                                .driverId(driver.getId())
                                .origin(orderRequestDTO.getOrigin())
                                .destination(orderRequestDTO.getDestination())
                                .departureTime(orderRequestDTO.getDeparture())
                                .status("pending")
                                .build();

                        return orderEntity;
                    });
                });
    }
}
