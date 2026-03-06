package io.github.bfur64.menu.item;

import java.io.IOException;

import io.github.bfur64.menu.MenuContext;

public class ActionItem extends Item {
    private final Actionable action;

    public ActionItem(String label, Actionable action) {
        super(label, true);
        this.action = action;
    }

    @Override
    public boolean onSelect(MenuContext menuContext)
            throws IOException {
        return action.run();
    }
}
