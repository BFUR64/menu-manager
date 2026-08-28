package io.github.bfur64.menu.item;

import io.github.bfur64.menu.event.MenuExitEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class ActionItem extends ButtonItem {
    private final Runnable action;
    private final boolean exitAfter;

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
    public @Nullable List<Item> runSelected() {
        action.run();

        if (exitAfter && event != null) {
            event.publish(new MenuExitEvent());
        }

        return null;
    }
}
