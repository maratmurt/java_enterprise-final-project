package ru.skillbox.paymentservice.handler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.web.client.MockRestServiceServer;
import ru.skillbox.event.OrderEvent;
import ru.skillbox.orderservice.domain.OrderDto;
import ru.skillbox.paymentservice.PaymentServiceAppTest;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

public class OrderEventHandlerTest extends PaymentServiceAppTest {

    @Test
    public void handleEventTest() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);
        mockServer.expect(requestTo("http://localhost:8080/api/order/1"))
                .andExpect(method(HttpMethod.PATCH))
                .andRespond(withStatus(HttpStatus.NO_CONTENT));

        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );

        OrderEvent orderEvent = new OrderEvent(1L, "User 1", orderDto);

        input.send(MessageBuilder.withPayload(orderEvent).build(), "order");
        Message<byte[]> outputMessage = output.receive(1500L, "payment");

        assertThat(outputMessage).isNotNull();
        mockServer.verify();
    }

}
