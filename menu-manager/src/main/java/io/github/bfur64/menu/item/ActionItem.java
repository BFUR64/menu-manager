package io.github.bfur64.menu.item;

import io.github.bfur64.menu.MenuContext;

public class ActionItem extends Item {
    private final Actionable action;

    public ActionItem(String label, Actionable action) {
        super(label, true);
        this.action = action;
    }

    public ActionItem(String label, Actionable action, boolean exitRequested) {
        this(label, action);
        this.exitRequested = true;
    }

    @Override
    public void selectItem(MenuContext menuContext) {
        action.run();
    }
}
