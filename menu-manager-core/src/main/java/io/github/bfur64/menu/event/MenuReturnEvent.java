package io.github.bfur64.menu.event;

import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public record MenuReturnEvent(List<Item> itemList) {}
