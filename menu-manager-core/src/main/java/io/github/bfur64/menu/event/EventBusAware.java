package io.github.bfur64.menu.event;

import io.github.bfur64.menu.Event;
import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface EventBusAware {
    void setEventBus(Event event);
}
