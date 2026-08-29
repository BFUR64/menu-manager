package io.github.bfur64.menu.event;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface EventListener<T> {
    void onEvent(T event);
}
