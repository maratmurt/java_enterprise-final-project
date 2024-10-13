package ru.skillbox.paymentservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.kafka.OrderEvent;
import ru.skillbox.kafka.PaymentEvent;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.StatusDto;
import ru.skillbox.paymentservice.domain.Account;
import ru.skillbox.paymentservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.repository.AccountRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent, PaymentEvent> {

    private final AccountRepository accountRepository;

    private final RestTemplate restTemplate;

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Override
    public PaymentEvent handleEvent(OrderEvent orderEvent) {
        log.info("Order event received: {}", orderEvent);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Long orderId = orderEvent.getOrderId();
        PaymentEvent paymentEvent = PaymentEvent.builder()
                .orderId(orderId)
                .build();

        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.PAYMENT_SERVICE);

        Account account = accountRepository.findByUsername(orderEvent.getUsername()).orElseThrow();
        Double balance = account.getBalance();
        Double cost = orderEvent.getCost();

        if (balance > cost) {
            account.setBalance(balance - cost);
            accountRepository.save(account);
            paymentEvent.setStatus(PaymentStatus.APPROVED.name());
            statusDto.setStatus(OrderStatus.PAID);
        } else {
            paymentEvent.setStatus(PaymentStatus.DECLINED.name());
            statusDto.setStatus(OrderStatus.PAYMENT_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + orderId, HttpMethod.PATCH, new HttpEntity<>(statusDto), Void.class);

        return paymentEvent;
    }
}
