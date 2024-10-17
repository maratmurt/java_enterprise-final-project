package ru.skillbox.event;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class OrderEvent implements Event {

    private Long orderId;

    private String username;

    private Double cost;

    private Map<Long, Integer> products;

    @Override
    public String getEvent() {
        return "OrderCreated";
    }

}
