package com.gianvittorio.drivermatchingservice.web.controller;

import com.gianvittorio.common.web.dto.drivers.DriverRequestDTO;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import com.gianvittorio.drivermatchingservice.service.DriversService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/drivers")
@Log4j2
@RequiredArgsConstructor
public class DriversController {

    private final DriversService driversService;

    @GetMapping(path = "/{document}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DriverResponseDTO>> findDriverByDocument(@PathVariable("document") final String document) {
        return driversService.findByDocument(document)
                .map(driverEntity -> {
                    final DriverResponseDTO driverResponseDTO = new DriverResponseDTO();
                    BeanUtils.copyProperties(driverEntity, driverResponseDTO);

                    return driverResponseDTO;
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
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
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DriverResponseDTO>> createDriver(@RequestBody DriverRequestDTO driverRequestDTO) {
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
                .map(ResponseEntity::ok);
    }

    @PutMapping(path = "/{document}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<DriverResponseDTO>> updateDriver(@PathVariable("document") final String document, @RequestBody DriverRequestDTO driverRequestDTO) {
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
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{document}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> updateDriver(@PathVariable("document") final String document) {
        return driversService.deleteByDocument(document);
    }
}
