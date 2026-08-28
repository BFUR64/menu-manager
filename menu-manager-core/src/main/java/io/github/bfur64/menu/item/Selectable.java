package io.github.bfur64.menu.item;

import org.jspecify.annotations.NullMarked;

@NullMarked
@FunctionalInterface
public interface Selectable {
    void selectItem();
}
