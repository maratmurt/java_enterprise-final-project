package ru.skillbox.event;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
@Builder
public class PaymentEvent implements Event {

    private Long orderId;

    private String paymentStatus;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "Payment";
    }
}
