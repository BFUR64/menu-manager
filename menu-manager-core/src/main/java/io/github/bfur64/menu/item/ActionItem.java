package io.github.bfur64.menu.item;

import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.EventBusAware;
import io.github.bfur64.menu.event.MenuExitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class ActionItem extends SelectableItem implements EventBusAware {
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
        action.run();

        if (exitAfter && event != null) {
            event.publish(new MenuExitEvent());
        }

        return null;
    }

    @Override
    public void setEventBus(Event event) {
        this.event = event;
    }
}
