package io.github.bfur64.menu;

import io.github.bfur64.menu.event.CursorChangeEvent;
import io.github.bfur64.menu.event.MenuEnterEvent;
import io.github.bfur64.menu.event.MenuReturnEvent;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class MenuCursor {
    private final Event event;

    private final List<Integer> cursorPositions = new ArrayList<>();

    private int cursorPosition = -1;
    private List<Item> itemList = List.of();

    public MenuCursor(Event event) {
        this.event = event;

        event.subscribe(MenuEnterEvent.class, e -> {
            itemList = e.itemList();

            initCursorPosition();
            cursorPositions.addLast(cursorPosition);

            event.publish(new CursorChangeEvent(cursorPosition));
        });

        event.subscribe(MenuReturnEvent.class, e -> {
            itemList = e.itemList();

            cursorPositions.removeLast();
            cursorPosition = cursorPositions.getLast();

            event.publish(new CursorChangeEvent(cursorPosition));
        });
    }

    private void initCursorPosition() {
        for (int itemIndex = 0; itemIndex < itemList.size(); itemIndex++) {
            if (itemList.get(itemIndex) instanceof SelectableItem) {
                cursorPosition = itemIndex;
                return;
            }
        }

        cursorPosition = -1;
    }

    public void moveCursor(int cursorMovement) {
        if (itemList.isEmpty()) return;

        do {
            cursorPosition += cursorMovement;

            if (cursorPosition < 0) cursorPosition = itemList.size() - 1;

            if (cursorPosition > itemList.size() - 1) cursorPosition = 0;
        }
        while (!(itemList.get(cursorPosition) instanceof SelectableItem));

        cursorPositions.set(cursorPositions.size() - 1, cursorPosition);

        event.publish(new CursorChangeEvent(cursorPosition));
    }
}
