package net.i2i.kotam.handler;

public interface EventHandler<T> {
    void handle(T event);
}