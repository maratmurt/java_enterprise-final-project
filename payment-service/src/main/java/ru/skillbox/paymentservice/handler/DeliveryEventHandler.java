package ru.skillbox.paymentservice.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.orderservice.domain.DeliveryStatus;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.orderservice.domain.PaymentStatus;
import ru.skillbox.paymentservice.service.PaymentService;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeliveryEventHandler implements EventHandler<DeliveryEvent, PaymentEvent> {

    private final PaymentService paymentService;

    @Override
    public PaymentEvent handleEvent(DeliveryEvent deliveryEvent) {
        log.info("Received delivery event: {}", deliveryEvent);

        if (deliveryEvent.getDeliveryStatus().equals(DeliveryStatus.DELIVERED.name())) {
            return null;
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Long orderId = deliveryEvent.getOrderId();
        OrderDto orderDto = deliveryEvent.getOrderDto();
        String username = deliveryEvent.getUsername();

        paymentService.refundOrder(username, orderId, orderDto.getCost());

        PaymentEvent paymentEvent = new PaymentEvent();
        paymentEvent.setOrderId(orderId);
        paymentEvent.setOrderDto(orderDto);
        paymentEvent.setPaymentStatus(PaymentStatus.REFUND.name());

        return paymentEvent;
    }

}
