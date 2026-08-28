package io.github.bfur64.menu.item;

import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.EventBusAware;
import io.github.bfur64.menu.event.ItemSelectChangeEvent;
import io.github.bfur64.menu.event.MenuExitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NullMarked
public final class ActionItem extends SelectableItem implements EventBusAware {
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    private final Runnable action;
    private final boolean exitAfter;

    private @Nullable Event event;

    public ActionItem(String name, Runnable action) {
        this(name, action, false);
    }

    public ActionItem(String name, boolean exitAfter) {
        this(name, () -> {}, exitAfter);
    }

    public ActionItem(String name, Runnable action, boolean exitAfter) {
        super(name);
        this.action = action;
        this.exitAfter = exitAfter;
    }

    @Override
    public @Nullable List<Item> selectItem() {
        if (event != null) {
            event.publish(new ItemSelectChangeEvent(this));
        }

        action.run();

        if (exitAfter && event != null) {
            event.publish(new MenuExitEvent());
        }

        scheduler.schedule(
            () -> {
                if (event != null) {
                    event.publish(new ItemSelectChangeEvent(null));
                }
            },
            100,
            TimeUnit.MILLISECONDS
        );

        return null;
    }

    @Override
    public void setEventBus(Event event) {
        this.event = event;
    }
}
