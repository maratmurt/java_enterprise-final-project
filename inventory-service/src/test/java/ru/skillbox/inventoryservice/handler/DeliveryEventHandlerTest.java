package ru.skillbox.inventoryservice.handler;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.inventoryservice.InventoryServiceAppTest;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DeliveryEventHandlerTest extends InventoryServiceAppTest {

    @Test
    public void handleEventTest() {
        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );

        DeliveryEvent deliveryEvent = new DeliveryEvent(
                1L,
                "User 1",
                "RETURNED",
                orderDto
        );

        input.send(MessageBuilder.withPayload(deliveryEvent).build(), "delivery");
        Message<byte[]> outputMessage = output.receive(1500L, "inventory");

        assertThat(outputMessage).isNotNull();
    }

}
