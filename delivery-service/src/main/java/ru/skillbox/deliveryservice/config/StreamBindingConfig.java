package ru.skillbox.deliveryservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.DeliveryEvent;
import ru.skillbox.event.EventHandler;
import ru.skillbox.event.InventoryEvent;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class StreamBindingConfig {

    private final EventHandler<InventoryEvent, DeliveryEvent> inventoryEventHandler;

    @Bean
    public Function<InventoryEvent, DeliveryEvent> inventoryEventProcessor() {
        return inventoryEventHandler::handleEvent;
    }

}
