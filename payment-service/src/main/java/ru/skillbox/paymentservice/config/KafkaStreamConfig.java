package ru.skillbox.paymentservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.skillbox.kafka.OrderEvent;
import ru.skillbox.kafka.PaymentEvent;
import ru.skillbox.paymentservice.handler.EventHandler;

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
