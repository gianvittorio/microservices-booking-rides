package com.gianvittorio.orderservice.service;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.gianvittorio.common.web.dto.drivers.DriverResponseDTO;
import com.gianvittorio.common.web.dto.users.UserResponseDTO;
import com.gianvittorio.orderservice.domain.Invoice;
import com.gianvittorio.orderservice.domain.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@Log4j2
@RequiredArgsConstructor
public class SchedulerService {

    private final JavaMailSender mailSender;

    private final OrdersService ordersService;

    private final UsersService usersService;

    private final DriversService driversService;

    private final ObjectWriter objectWriter;

    @Scheduled(cron = "0 * * * * *")
    public void schedule() {

        final LocalDateTime start = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        final LocalDateTime end = start.plusMinutes(30);

        final Flux<OrderEntity> orderEntityFlux = ordersService.findByStatusAndDepartureTimeBetween("pending", start, end);

        Flux<Invoice> invoiceFlux = orderEntityFlux.flatMap(orderEntity -> {

                    final Mono<String> driverNameMono = driversService.findById(orderEntity.getDriverId())
                            .map(DriverResponseDTO::getFirstname);

                    final Mono<String> userEmailMono = usersService.findUserById(orderEntity.getPassengerId())
                            .map(UserResponseDTO::getEmail);

                    final Mono<Invoice> invoiceMono = userEmailMono.zipWith(driverNameMono,
                            (userEmail, driverName) -> Invoice.builder()
                                    .orderId(orderEntity.getId())
                                    .origin(orderEntity.getOrigin())
                                    .destination(orderEntity.getDestination())
                                    .driverName(driverName)
                                    .departure(orderEntity.getDepartureTime())
                                    .createdAt(LocalDateTime.now(ZoneId.of("America/Sao_Paulo")))
                                    .build());

                    return invoiceMono;
                })
                .doOnNext(this::send);


        orderEntityFlux.zipWith(invoiceFlux, (orderEntity, invoice) -> orderEntity)
                .flatMap(orderEntity -> {
                    orderEntity.setStatus("successful");

                    return ordersService.save(orderEntity);
                })
                .subscribe();
    }

    private void send(final Invoice invoice) {

//        final SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("noreply@bookingrides.com");
//        message.setTo(invoice.getUserEmail());
//        message.setSubject("Invoice No ".concat(String.valueOf(invoice.getOrderId())));

        try {

            String body = objectWriter.writeValueAsString(invoice);
            log.info(body);
            //message.setText(body);

            //mailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());

            //throw new RuntimeException(e);
        }
    }
}
