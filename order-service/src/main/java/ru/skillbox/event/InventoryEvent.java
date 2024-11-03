package ru.skillbox.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.skillbox.orderservice.domain.OrderDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryEvent implements Event {

    private Long orderId;

    private String username;

    private String inventoryStatus;

    private OrderDto orderDto;

    @Override
    public String getEvent() {
        return "Inventory";
    }

}
