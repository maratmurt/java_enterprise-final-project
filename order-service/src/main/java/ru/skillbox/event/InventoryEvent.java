package ru.skillbox.event;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryEvent implements Event {

    private Long orderId;

    private String status;

    @Override
    public String getEvent() {
        return "Inventory";
    }

}
