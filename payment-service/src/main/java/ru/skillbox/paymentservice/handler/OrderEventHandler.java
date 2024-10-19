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
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.OrderStatus;
import ru.skillbox.orderservice.domain.ServiceName;
import ru.skillbox.orderservice.domain.StatusDto;
import ru.skillbox.orderservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent, PaymentEvent> {

    private final RestTemplate restTemplate;

    private final PaymentService paymentService;

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
        OrderDto orderDto = orderEvent.getOrderDto();
        String username = orderEvent.getUsername();

        StatusDto statusDto = new StatusDto();
        statusDto.setServiceName(ServiceName.PAYMENT_SERVICE);

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderId);
        paymentEvent.setOrderDto(orderDto);

        if (paymentService.payForOrder(orderDto, username)) {
            paymentEvent.setPaymentStatus(PaymentStatus.APPROVED.name());
            statusDto.setStatus(OrderStatus.PAID);
        } else {
            paymentEvent.setPaymentStatus(PaymentStatus.DECLINED.name());
            statusDto.setStatus(OrderStatus.PAYMENT_FAILED);
        }
        restTemplate.exchange(orderServiceUrl + orderId, HttpMethod.PATCH, new HttpEntity<>(statusDto), Void.class);

        return paymentEvent;
    }
}
