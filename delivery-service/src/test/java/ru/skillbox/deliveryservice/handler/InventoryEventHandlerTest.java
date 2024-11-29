package ru.skillbox.deliveryservice.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.skillbox.deliveryservice.DeliveryServiceAppTest;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.orderservice.domain.InventoryStatus;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class InventoryEventHandlerTest extends DeliveryServiceAppTest {

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Test
    public void handleEventTest() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo(orderServiceUrl + "1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        OrderDto order = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList());
        InventoryEvent inventoryEvent = new InventoryEvent(
                1L,
                "User 1",
                InventoryStatus.COMPLETE.name(),
                order);

        input.send(MessageBuilder.withPayload(inventoryEvent).build(), "inventory");
        Message<byte[]> outputMessage = output.receive(1500L, "delivery");

        Integer actualDeliveryCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(1) FROM delivery WHERE order_id = 1",
                Integer.class
        );

        assertThat(outputMessage).isNotNull();
        assertThat(actualDeliveryCount).isEqualTo(1L);
        mockServer.verify();
    }

}
