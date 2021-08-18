package com.gianvittorio.orderservice.service;

import com.gianvittorio.orderservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Log4j2
@RequiredArgsConstructor
public class SchedulerService {

    private final OrdersRepository ordersRepository;

    @Scheduled(fixedRate = 5000)//(cron = "0 * * * * *")
    public void schedule() {

        final LocalDateTime start = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        final LocalDateTime end = start.plusMinutes(30);

        ordersRepository.findByStatusAndDepartureTimeBetween("pending", start, end)
                .collectList()
                .doOnNext(orderEntityList -> log.info("Accusi: {}, {}, {}", orderEntityList.size(), start, end))
                .subscribe();
    }
}
