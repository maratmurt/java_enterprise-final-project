package ru.skillbox.event;

import lombok.Data;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
public class DeliveryEvent implements Event {

    private Long orderId;

    private String username;

    private String deliveryStatus;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "Delivery";
    }
}
