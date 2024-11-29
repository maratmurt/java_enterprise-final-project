package ru.skillbox.paymentservice.handler;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.PaymentServiceAppTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class InventoryEventHandlerTest extends PaymentServiceAppTest {

    @Test
    public void handleEventTest() {
        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );

        InventoryEvent inventoryEvent = new InventoryEvent(
                1L,
                "User 1",
                "INCOMPLETE",
                orderDto
        );

        input.send(MessageBuilder.withPayload(inventoryEvent).build(), "inventory");
        Message<byte[]> outputMessage = output.receive(1500L, "payment");

        assertThat(outputMessage).isNotNull();
    }

}
