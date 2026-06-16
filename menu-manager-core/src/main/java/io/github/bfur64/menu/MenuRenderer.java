package io.github.bfur64.menu;

import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
public class MenuRenderer {
    private final TerminalBackend terminal;
    private final List<Item> menuItems;
    private final MenuCursor cursor;
    private final int itemIndent;

    private @Nullable Item highlightedItem;

    private final int selectableItemCount;

    private @Nullable Popup popup;

    public MenuRenderer(TerminalBackend terminal, List<Item> menuItems, MenuCursor cursor, int itemIndent) {
        this.terminal = terminal;
        this.menuItems = menuItems;
        this.cursor = cursor;
        this.itemIndent = itemIndent;

        this.selectableItemCount = countSelectableItems(menuItems);
    }

    public void update() {
        terminal.clearScreen();

        drawMenu();
        drawCursor();
        drawPopup();

        terminal.flush();
    }

    private void drawMenu() {
        for (int i = 0; i < menuItems.size(); i++) {
            Item item = menuItems.get(i);

            if (Objects.equals(highlightedItem, item)) {
                terminal.put(itemIndent, i, item.getDisplayName());

                terminal.setBackgroundColor(255, 255, 255);
                terminal.setForegroundColor(0, 0, 0);
                terminal.put(itemIndent, i, item.getName());
                terminal.resetColorAndStyle();
            }
            else {
                terminal.put(itemIndent, i, menuItems.get(i).getDisplayName());
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
