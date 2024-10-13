package ru.skillbox.kafka;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentEvent implements Event {

    private Long orderId;

    private String status;

    @Override
    public String getEvent() {
        return "Payment";
    }
}
