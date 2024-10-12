package ru.skillbox.orderservice.service;

import ru.skillbox.kafka.Event;

public interface KafkaService {

    void produce(Event event);

}
