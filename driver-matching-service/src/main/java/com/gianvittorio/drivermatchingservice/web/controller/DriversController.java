package com.gianvittorio.drivermatchingservice.web.controller;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import com.gianvittorio.drivermatchingservice.service.DriversService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("/drivers")
@Log4j2
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    @GetMapping(path = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find driver by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Mono<ResponseEntity<DriverResponseDTO>> findDriverById(@PathVariable("id") final Long id) {
        return driversService.findById(id)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @GetMapping(path = "/document/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find driver by document")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Mono<ResponseEntity<DriverResponseDTO>> findDriverByDocument(@PathVariable("document") final String document) {
        return driversService.findByDocument(document)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @GetMapping(path = "/search", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Find very first driver by category, location and rating")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public Mono<ResponseEntity<DriverResponseDTO>> findFirstAvailableDriver(
            @RequestParam("category") final String category,
            @RequestParam("location") final String location,
            @RequestParam("rating") final Integer rating
    ) {
        return driversService.findFirstAvailable(category, location, rating)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver set for creation"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<DriverResponseDTO>> createDriver(@Valid  @RequestBody DriverRequestDTO driverRequestDTO) {
        return Mono.just(driverRequestDTO)
                .map(requestDTO -> {
                    final DriverEntity driverEntity = new DriverEntity();
                    BeanUtils.copyProperties(requestDTO, driverEntity);

                    return driverEntity;
                })
                .flatMap(driversService::save)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @PutMapping(path = "/{document}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Driver set for creation"),
            @ApiResponse(responseCode = "404", description = "Driver not found"),
            @ApiResponse(responseCode = "400", description = "Request body is either null or malformed")
    })
    public Mono<ResponseEntity<DriverResponseDTO>> updateDriver(@PathVariable("document") final String document, @Valid @RequestBody DriverRequestDTO driverRequestDTO) {
        return driversService.findByDocument(document)
                .map(driverEntity -> {
                    BeanUtils.copyProperties(driverRequestDTO, driverEntity);

                    return driverEntity;
                })
                .flatMap(driversService::save)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorMap(error -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error));
    }

    @DeleteMapping(path = "/id/{id}")
    @Operation(summary = "Delete driver by Id")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteDriverById(@PathVariable("id") final Long id) {
        return driversService.deleteById(id);
    }

    @DeleteMapping(path = "/document/{document}")
    @Operation(summary = "Delete driver by document")
    @ApiResponse(responseCode = "204", description = "No content")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateDriver(@PathVariable("document") final String document) {
        return driversService.deleteByDocument(document);
    }
}
