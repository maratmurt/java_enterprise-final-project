package ru.skillbox.event;

import lombok.Builder;
import lombok.Data;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
@Builder
public class OrderEvent implements Event {

    private Long orderId;

    private String username;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "OrderCreated";
    }

}
