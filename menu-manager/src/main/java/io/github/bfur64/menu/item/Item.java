package io.github.bfur64.menu.item;

import io.github.bfur64.menu.MenuContext;

public abstract class Item {
    protected final String label;
    protected final boolean isSelectable;

    protected Item(String label, boolean isSelectable) {
        this.label = label;
        this.isSelectable = isSelectable;
    }

    public String getLabel() { return label; }
    public boolean isSelectable() { return isSelectable; }

    public String getDisplayName() { return label; }

    public boolean onSelect(MenuContext mc) {
        if (!isSelectable) {
            throw new IllegalStateException("Tried to select a non-selectable item: " + label);
        }

        // Default that tells the MenuManager to continue the loop
        return true;
    }
}
