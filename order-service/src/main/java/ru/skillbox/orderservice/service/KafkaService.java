package ru.skillbox.orderservice.service;

import ru.skillbox.event.Event;

public interface KafkaService {

    void produce(Event event);

}
