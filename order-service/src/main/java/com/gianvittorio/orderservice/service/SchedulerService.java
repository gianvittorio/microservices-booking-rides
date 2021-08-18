package com.gianvittorio.orderservice.service;

import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import com.gianvittorio.orderservice.repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

@Service
@Log4j2
@RequiredArgsConstructor
public class SchedulerService {

    private final JavaMailSender mailSender;

    private final OrdersRepository ordersRepository;

    @Scheduled(cron = "0 * * * * *")
    public void schedule() {

        final LocalDateTime start = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        final LocalDateTime end = start.plusMinutes(30);

        ordersRepository.findByStatusAndDepartureTimeBetween("pending", start, end)
                .collectList()
                .doOnNext(this::sendToAll)
                .subscribe();
    }

    private void sendToAll(final Collection<? extends OrderEntity> orders) {

        log.info("Accusi: {}", orders.size());

        orders.forEach(order -> {
            final SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("tobeto1183@error57.com");
            message.setTo("tobeto1183@error57.com");
            message.setSubject("Notification");
            message.setText(String.format("Id: %s; origin: %s; destination: %s; departure: %s", order.getId(), order.getOrigin(), order.getDestination(), order.getDepartureTime().toString()));
            mailSender.send(message);
        });
    }
}
