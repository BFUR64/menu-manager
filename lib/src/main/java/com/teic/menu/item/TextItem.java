package com.teic.menu.item;

import com.teic.menu.item.Item;

public class TextItem extends Item {
    public TextItem(String text) {
        super(text, false);
    }

    public static TextItem breakItem() {
        return new TextItem("");
    }
}
