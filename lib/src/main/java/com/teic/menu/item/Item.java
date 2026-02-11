package com.teic.menu.item;

import java.io.IOException;

import com.teic.menu.MenuContext;

public abstract class Item {
    protected final String label;
    protected final boolean selectable;

    protected Item(String label, boolean selectable) {
        this.label = label;
        this.selectable = selectable;
    }

    public String getLabel() { return label; }
    public boolean selectable() { return selectable; }

    public String getDisplayName() { return label; }

    public boolean onSelect(MenuContext mc)
            throws IOException {
        if (!selectable) {
            throw new IllegalStateException("Tried to select a non-selectable item: " + label);
        }

        // Default that tells the MenuManager to continue the loop
        return true;
    }
}
