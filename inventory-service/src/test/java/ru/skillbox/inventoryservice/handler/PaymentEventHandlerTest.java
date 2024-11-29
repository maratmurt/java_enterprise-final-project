package ru.skillbox.inventoryservice.handler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.inventoryservice.InventoryServiceAppTest;
import ru.skillbox.orderservice.domain.OrderDto;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class PaymentEventHandlerTest extends InventoryServiceAppTest {

    @Value("${service.order.url}")
    private String orderServiceUrl;

    @Test
    public void handleEventTest() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo(orderServiceUrl + "1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );

        PaymentEvent paymentEvent = new PaymentEvent(
                1L,
                "User 1",
                "APPROVED",
                orderDto
        );

        input.send(MessageBuilder.withPayload(paymentEvent).build(), "payment");
        Message<byte[]> outputMessage = output.receive(1500L, "inventory");

        assertThat(outputMessage).isNotNull();
        mockServer.verify();
    }

}
