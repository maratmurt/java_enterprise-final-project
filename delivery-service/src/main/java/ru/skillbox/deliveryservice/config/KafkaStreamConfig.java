package ru.skillbox.deliveryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.EventConsumer;
import ru.skillbox.event.InventoryEvent;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {

    private final EventConsumer<InventoryEvent> inventoryEventConsumer;

    @Bean
    public Consumer<InventoryEvent> inventoryEventProcessor() {
        return inventoryEventConsumer::consumeEvent;
    }

}
