package io.github.bfur64.menu.item;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ActionItem extends SelectableItem {
    private final Runnable action;

    public ActionItem(String name, Runnable action) {
        super(name);
        this.action = action;
    }

    public ActionItem(String name, boolean shouldExit) {
        this(name, () -> {}, shouldExit);
    }

    public ActionItem(String name, Runnable action, boolean shouldExit) {
        super(name);
        this.action = action;
        this.shouldExit = shouldExit;
    }

    @Override
    public void selectItem() {
        action.run();
    }
}
