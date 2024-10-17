package ru.skillbox.event;

public interface EventHandler<T extends Event, R extends Event> {

    R handleEvent(T event);

}
