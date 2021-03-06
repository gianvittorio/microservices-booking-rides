package com.gianvittorio.drivermatchingservice.service.impl;

import com.gianvittorio.drivermatchingservice.domain.entity.DriverEntity;
import com.gianvittorio.drivermatchingservice.repository.DriversRepository;
import com.gianvittorio.drivermatchingservice.service.DriversService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class DriversServiceImpl implements DriversService {

    final private DriversRepository driversRepository;

    @Override
    public Mono<DriverEntity> findById(Long id) {
        return driversRepository.findById(id);
    }

    @Override
    public Mono<DriverEntity> findFirstAvailable(final String category, final String location, final Integer rating) {
        return driversRepository.findFirstAvailable(category, location, rating);
    }

    @Override
    public Mono<DriverEntity> findByDocument(final String document) {
        return driversRepository.findByDocument(document).next();
    }

    @Override
    public Mono<DriverEntity> save(final DriverEntity driverEntity) {
        return driversRepository.save(driverEntity);
    }

    @Override
    public Mono<Void> deleteByDocument(final String document) {
        return driversRepository.deleteByDocument(document);
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return driversRepository.deleteById(id);
    }
}
