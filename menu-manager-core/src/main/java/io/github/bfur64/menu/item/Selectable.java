package io.github.bfur64.menu.item;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public interface Selectable {
    @Nullable List<Item> selectItem();
}
