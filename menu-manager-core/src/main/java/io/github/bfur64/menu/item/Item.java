package io.github.bfur64.menu.item;

import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class Item {
    protected final String name;

    protected Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return name;
    }
}
