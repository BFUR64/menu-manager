package io.github.bfur64.menu.item.display;

import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

@NullMarked
public class DynamicText<T> extends Item {
    private final String suffix;
    private final Supplier<T> supplier;

    public DynamicText(String name, Supplier<T> supplier) {
        this(name, "", supplier);
    }

    public DynamicText(String name, String suffix, Supplier<T> supplier) {
        super(name);
        this.suffix = suffix;
        this.supplier = supplier;
    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName() + supplier.get() + " " + suffix;
    }
}
