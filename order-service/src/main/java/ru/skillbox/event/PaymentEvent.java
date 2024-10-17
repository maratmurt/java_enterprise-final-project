package ru.skillbox.event;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PaymentEvent implements Event {

    private Long orderId;

    private String status;

    private Map<Long, Integer> products;

    @Override
    public String getEvent() {
        return "Payment";
    }
}
