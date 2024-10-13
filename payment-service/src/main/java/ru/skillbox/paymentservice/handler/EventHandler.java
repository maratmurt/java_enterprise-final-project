package ru.skillbox.paymentservice.handler;

import ru.skillbox.kafka.Event;

public interface EventHandler<T extends Event, R extends Event> {

    R handleEvent(T event);

}
