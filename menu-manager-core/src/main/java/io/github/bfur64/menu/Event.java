package io.github.bfur64.menu;

import io.github.bfur64.menu.event.EventListener;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public final class Event {
    private final Map<Class<?>, List<Dispatcher<?>>> listeners = new HashMap<>();

    public <T> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners
            .computeIfAbsent(eventType, ignore -> new ArrayList<>())
            .add(new Dispatcher<>(eventType, listener));
    }

    public <T> void publish(T event) {
        List<Dispatcher<?>> listenersForEvent = listeners.get(event.getClass());

        if (listenersForEvent == null) {
            return;
        }

        for (Dispatcher<?> listener : listenersForEvent) {
            listener.dispatch(event);
        }
    }
}
