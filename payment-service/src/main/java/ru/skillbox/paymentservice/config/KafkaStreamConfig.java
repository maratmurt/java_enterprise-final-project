package ru.skillbox.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.event.OrderEvent;
import ru.skillbox.event.PaymentEvent;
import ru.skillbox.event.EventHandler;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class KafkaStreamConfig {

    private final EventHandler<OrderEvent, PaymentEvent> orderEventHandler;

    @Bean
    public Function<OrderEvent, PaymentEvent> orderEventProcessor() {
        return orderEventHandler::handleEvent;
    }

}
