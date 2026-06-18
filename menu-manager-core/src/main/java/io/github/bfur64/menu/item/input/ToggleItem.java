package io.github.bfur64.menu.item.input;

import io.github.bfur64.menu.Property;
import io.github.bfur64.menu.item.SelectableItem;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ToggleItem extends SelectableItem {
    private static final String TOGGLE_ON = "■";
    private static final String TOGGLE_OFF = "□";

    private final Property<Boolean> property;

    public ToggleItem(String name, Property<Boolean> property) {
        super(name);
        this.property = property;
    }

    @Override
    public String getDisplayName() {
        return (property.get() ? TOGGLE_ON : TOGGLE_OFF) + " " + name;
    }

    @Override
    public void selectItem() {
        property.set(!property.get());
    }
}
