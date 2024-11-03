package ru.skillbox.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEvent implements Event {

    private Long orderId;

    private String username;

    private String paymentStatus;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "Payment";
    }
}
