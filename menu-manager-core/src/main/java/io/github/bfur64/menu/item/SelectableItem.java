package io.github.bfur64.menu.item;

public abstract class SelectableItem extends Item implements Selectable {

    protected SelectableItem(String name) {
        super(name);
    }
}
