package io.github.bfur64.menu;

import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
public class MenuRenderer {
    private final Terminal terminal;
    private List<Item> menuItems;
    private final MenuCursor cursor;
    private final int itemIndent;

    private @Nullable Item highlightedItem;

    private final int selectableItemCount;

    private @Nullable Popup popup;

    public MenuRenderer(Terminal terminal, List<Item> menuItems, MenuCursor cursor, int itemIndent) {
        this.terminal = terminal;
        this.menuItems = menuItems;
        this.cursor = cursor;
        this.itemIndent = itemIndent;

        this.selectableItemCount = countSelectableItems(menuItems);
    }

    public void update() {
        terminal.clear();

        drawMenu();
        drawCursor();
        drawPopup();

        terminal.flush();
    }

    public void replaceItems(List<Item> menuItems) {
        this.menuItems = menuItems;
    }

    private void drawMenu() {
        for (int i = 0; i < menuItems.size(); i++) {
            Item item = menuItems.get(i);

            if (Objects.equals(highlightedItem, item)) {
                terminal.put(itemIndent, i, item.getDisplayName());

                terminal.onSGR(SGR.REVERSE);
                terminal.put(itemIndent, i, item.getName());
                terminal.offSGR(SGR.REVERSE);
            }
            else {
                terminal.put(itemIndent, i, item.getDisplayName());
            }
        }
    }

    private void drawCursor() {
        if (selectableItemCount == 0) return;

        terminal.put(cursor.getPosition().x(), cursor.getPosition().y(), cursor.getCursorSymbol());
    }

    private void drawPopup() {
        if (popup == null) return;

        popup.draw();
    }

    private int countSelectableItems(List<Item> items) {
        return (int) items.stream()
            .filter(SelectableItem.class::isInstance)
            .count();
    }

    public void setPopup(@Nullable Popup popup) {
        this.popup = popup;
    }

    public void setHighlightedItem(@Nullable Item highlightedItem) {
        this.highlightedItem = highlightedItem;
    }
}
