package ru.skillbox.event;

import lombok.Data;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
public class InventoryEvent implements Event {

    private Long orderId;

    private String inventoryStatus;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "Inventory";
    }

}
