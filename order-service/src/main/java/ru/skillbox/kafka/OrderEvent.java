package ru.skillbox.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderEvent implements Event {

    private Long orderId;

    private String username;

    private Double cost;

    @Override
    public String getEvent() {
        return "OrderCreated";
    }

}
