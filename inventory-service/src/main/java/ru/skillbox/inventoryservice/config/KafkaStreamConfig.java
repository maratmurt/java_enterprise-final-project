package ru.skillbox.inventoryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;
import ru.skillbox.event.PaymentEvent;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {

    private final EventHandler<PaymentEvent, InventoryEvent> paymentEventHandler;

    @Bean
    public Function<PaymentEvent, InventoryEvent> paymentEventProcessor() {
        return paymentEventHandler::handleEvent;
    }

}
