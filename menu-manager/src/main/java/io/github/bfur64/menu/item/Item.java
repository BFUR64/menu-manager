package io.github.bfur64.menu.item;

public abstract class Item {
    protected final String label;
    protected final boolean isSelectable;
    protected boolean exitRequested;

    protected Item(String label, boolean isSelectable) {
        this.label = label;
        this.isSelectable = isSelectable;
    }

    public String getLabel() { return label; }
    public boolean isSelectable() { return isSelectable; }

    public String getDisplayName() { return label; }

    public void selectItem() {}

    public boolean exitRequested() {
        return exitRequested;
    }
}
