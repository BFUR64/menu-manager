package io.github.bfur64.menu.event;

import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record ItemSelectChangeEvent(@Nullable Item itemSelected) {}
