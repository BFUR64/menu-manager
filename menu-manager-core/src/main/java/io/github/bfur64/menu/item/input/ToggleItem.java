package io.github.bfur64.menu.item.input;

import io.github.bfur64.menu.Property;
import io.github.bfur64.menu.item.ButtonItem;
import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public final class ToggleItem extends ButtonItem {
    private static final String TOGGLE_ON = "■";
    private static final String TOGGLE_OFF = "□";

    private final Property<Boolean> property;

    public ToggleItem(String name, Property<Boolean> property) {
        super(name);
        this.property = property;
    }

    @Override
    public String getName() {
        return getDisplayName();
    }

    @Override
    public String getDisplayName() {
        return (property.get() ? TOGGLE_ON : TOGGLE_OFF) + " " + name;
    }

    @Override
    public @Nullable List<Item> runSelected() {
        property.set(!property.get());
        return null;
    }
}
