package ru.skillbox.inventoryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.inventoryservice.handler.DeliveryEventHandler;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {

    private final EventHandler<PaymentEvent, InventoryEvent> paymentEventHandler;

    private final EventHandler<DeliveryEvent, InventoryEvent> deliveryEventHandler;

    @Bean
    public Function<PaymentEvent, InventoryEvent> paymentEventProcessor() {
        return paymentEventHandler::handleEvent;
    }

    @Bean
    public Function<DeliveryEvent, InventoryEvent> deliveryEventProcessor() {
        return deliveryEventHandler::handleEvent;
    }

}
