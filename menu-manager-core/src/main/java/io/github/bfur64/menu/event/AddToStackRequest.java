package io.github.bfur64.menu.event;

import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public record AddToStackRequest(@Nullable List<Item> itemList) {}
