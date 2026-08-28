package io.github.bfur64.menu.event;

import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record ItemSelectEvent(Item itemSelected) {}
