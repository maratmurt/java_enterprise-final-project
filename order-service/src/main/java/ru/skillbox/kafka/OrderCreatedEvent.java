package ru.skillbox.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreatedEvent implements Event {

    private Long orderId;

    private String username;

    private Long cost;

    @Override
    public String getEvent() {
        return "OrderCreated";
    }

}
