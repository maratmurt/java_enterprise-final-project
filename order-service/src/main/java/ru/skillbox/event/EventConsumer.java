package ru.skillbox.event;

public interface EventConsumer<T extends Event> {

    void consumeEvent(T event);

}
