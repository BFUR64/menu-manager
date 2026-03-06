package io.github.bfur64.menu.item;

public class TextItem extends Item {
    public TextItem(String text) {
        super(text, false);
    }

    public static TextItem breakItem() {
        return new TextItem("");
    }
}
