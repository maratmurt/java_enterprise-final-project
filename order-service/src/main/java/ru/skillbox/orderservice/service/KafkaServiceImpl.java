package ru.skillbox.orderservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import ru.skillbox.kafka.Event;

@Service
public class KafkaServiceImpl implements KafkaService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaServiceImpl.class);

    @Value("${spring.kafka.topic}")
    private String kafkaTopic;

    private final StreamBridge streamBridge;

    public KafkaServiceImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void produce(Event event) {
        streamBridge.send(kafkaTopic, event);
        logger.info("Sent message to Kafka -> '{}'", event);
    }
}
