package io.github.bfur64.menu.item;

import io.github.bfur64.menu.MenuManager;
import org.jspecify.annotations.NullMarked;

import java.util.function.Consumer;

@NullMarked
public class ActionItem extends SelectableItem {

    private final Consumer<MenuManager> action;

    public ActionItem(String name, Consumer<MenuManager> action) {
        super(name);
        this.action = action;
    }

    public ActionItem(String name) {
        this(name, menuManager -> {});
    }

    @Override
    public void selectItem(MenuManager manager) {
        action.accept(manager);
    }
}
