package ru.skillbox.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.*;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {

    private final EventHandler<OrderEvent, PaymentEvent> orderEventHandler;

    private final EventHandler<InventoryEvent, PaymentEvent> inventoryEventHandler;

    private final EventHandler<DeliveryEvent, PaymentEvent> deliveryEventHandler;

    @Bean
    public Function<OrderEvent, PaymentEvent> orderEventProcessor() {
        return orderEventHandler::handleEvent;
    }

    @Bean
    public Function<InventoryEvent, PaymentEvent> inventoryEventProcessor() {
        return inventoryEventHandler::handleEvent;
    }

    @Bean
    public Function<DeliveryEvent, PaymentEvent> deliveryEventProcessor() {
        return deliveryEventHandler::handleEvent;
    }

}
