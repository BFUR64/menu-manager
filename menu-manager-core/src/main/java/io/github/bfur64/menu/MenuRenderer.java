package io.github.bfur64.menu;

import io.github.bfur64.menu.event.*;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
public final class MenuRenderer {
    private final Terminal terminal;

    private List<Item> itemList = List.of();
    private @Nullable Item itemSelected = null;

    private @Nullable PopupManager popup = null;

    private int cursorPosition = -1;

    public MenuRenderer(Terminal terminal, Event event) {
        this.terminal = terminal;

        event.subscribe(MenuEnterEvent.class, e -> itemList = e.itemList());
        event.subscribe(MenuReturnEvent.class, e -> itemList = e.itemList());

        event.subscribe(ItemSelectChangeEvent.class, e -> itemSelected = e.itemSelected());

        event.subscribe(CursorChangeEvent.class, e -> cursorPosition = e.cursorPosition());

        event.subscribe(PopupChangeEvent.class, e -> popup = e.popup());
    }

    public void render() {
        terminal.clear();

        drawMenu();
        drawCursor();

        if (popup != null) {
            popup.draw();
        }

        terminal.flush();
    }

    private void drawMenu() {
        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            int itemIndent = 3;

            if (Objects.equals(itemSelected, item)) {
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
        if (cursorPosition < 0) return;

        terminal.put(1, cursorPosition, ">");
    }
}
