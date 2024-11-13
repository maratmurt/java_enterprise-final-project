package ru.skillbox.orderservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import ru.skillbox.event.OrderEvent;
import ru.skillbox.orderservice.domain.*;
import ru.skillbox.orderservice.repository.OrderRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private StreamBridge streamBridge;

    @Value("${spring.kafka.topic}")
    private String topic;

    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order(
                "User 1",
                "Departure address",
                "Destination address",
                "Order description",
                100D,
                OrderStatus.REGISTERED
        );
        order.setId(1L);
    }

    @Test
    public void addOrderTest() {
        OrderDto orderDto = new OrderDto(
                "Order description",
                "Departure address",
                "Destination address",
                100D,
                Collections.emptyList()
        );
        OrderEvent event = new OrderEvent(1L, "User 1", orderDto);

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(streamBridge.send(topic, event)).thenReturn(true);

        Optional<Order> optional = orderService.addOrder(orderDto, "User 1");

        verify(orderRepository).save(any(Order.class));
        assertThat(optional).isNotNull();
        assertThat(optional).isPresent();
        assertThat(optional.get()).isEqualTo(order);
    }

    @Test
    public void updateOrderStatusTest() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        StatusDto statusDto = new StatusDto(
                OrderStatus.PAID,
                ServiceName.PAYMENT_SERVICE,
                "Order payment approved"
        );
        orderService.updateOrderStatus(1L, statusDto);

        verify(orderRepository).save(order);
    }

}
