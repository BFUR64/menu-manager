package io.github.bfur64.menu.item;

import org.jspecify.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface RunnableAction {
    @Nullable List<Item> run();
}
