package io.github.bfur64.menu.item;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class ListItem extends SelectableItem {
    private final RunnableAction action;

    public ListItem(String name, RunnableAction action) {
        super(name);
        this.action = action;
    }

    @Override
    public @Nullable List<Item> selectItem() {
        return action.run();
    }
}
