package io.github.bfur64.menu.item;

public abstract class SelectableItem extends Item implements Selectable {
    protected boolean shouldExit;

    protected SelectableItem(String name) {
        super(name);
    }

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }
}
