package ru.skillbox.paymentservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.OrderEvent;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.StatusDto;
import ru.skillbox.paymentservice.domain.Account;
import ru.skillbox.orderservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.domain.Transaction;
import ru.skillbox.paymentservice.repository.AccountRepository;
import ru.skillbox.paymentservice.repository.TransactionRepository;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent, PaymentEvent> {

    private final AccountRepository accountRepository;

    private final RestTemplate restTemplate;

    private final TransactionRepository transactionRepository;

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
        log.info("ACCOUNT: {}", account);
        Double balance = account.getBalance();
        Double cost = orderEvent.getCost();

        if (balance > cost) {
            paymentEvent.setStatus(PaymentStatus.APPROVED.name());
            statusDto.setStatus(OrderStatus.PAID);
            Transaction transaction = new Transaction();
            transaction.setAmount(-cost);
            transaction.setAccount(account);
            transaction.setDescription("Order" + orderId + " payment");
            transactionRepository.save(transaction);
            log.info("PAYMENT OF ${} COMPLETE", cost);
            Account updatedAccount = accountRepository.findByUsername(orderEvent.getUsername()).orElseThrow();
            log.info("UPDATED ACCOUNT: {}", updatedAccount);
        } else {
            paymentEvent.setStatus(PaymentStatus.DECLINED.name());
            statusDto.setStatus(OrderStatus.PAYMENT_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + orderId, HttpMethod.PATCH, new HttpEntity<>(statusDto), Void.class);

        return paymentEvent;
    }
}
