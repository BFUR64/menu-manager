package io.github.bfur64.menu;

import io.github.bfur64.Versions;
import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.menu.utils.ErrorEvent;
import io.github.bfur64.menu.utils.ErrorListener;
import io.github.bfur64.menu.utils.ErrorObservable;
import io.github.bfur64.menu.utils.Position;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

@NullMarked
public class MenuManager implements InputHandler, ErrorListener {
    private static final KeyStroke UNKNOWN_KEY = new KeyStroke(KeyType.UNKNOWN);
    private static final long NS_PER_FRAME = 1_000_000_000L / 60;

    private final TerminalBackend terminal;
    private final MenuCursor cursor;
    private final MenuRenderer renderer;

    private final List<Item> menuList;

    private @Nullable Item itemSelected;
    private @Nullable Popup popup;

    private boolean isRunning = true;

    public MenuManager(TerminalBackend terminal, List<Item> menuList) {
        this.terminal = terminal;
        this.menuList = menuList;

        for (Item item : menuList) {
            if (item instanceof ErrorObservable observableItem) {
                observableItem.setErrorListener(this);
            }
        }

        cursor = new MenuCursor(initCursorPosition(), ">");

        int itemIndent = cursor.getCursorSymbol().length() + 2;
        renderer = new MenuRenderer(terminal, menuList, cursor, itemIndent);
    }

    public void start() {
        while (isRunning) {
            long frameStart = System.nanoTime();

            // START
            KeyStroke keyStroke = terminal.pollInput();

            if (keyStroke == null) {
                keyStroke = UNKNOWN_KEY;
            }

            update(keyStroke);

            if (itemSelected != null &&
                itemSelected instanceof SelectableItem selectableItem &&
                selectableItem.shouldExit()
            ) {
                exit();
            }
            // END

            long deadline = frameStart + NS_PER_FRAME;
            long now = System.nanoTime();

            while (now < deadline) {
                LockSupport.parkNanos(deadline - now);
                now = System.nanoTime();
            }
        }
    }

    private void update(KeyStroke keyStroke) {
        if (itemSelected instanceof InputHandler inputItem && !inputItem.isFinished()) {
            inputItem.handle(keyStroke);

            if (inputItem.isFinished()) {
                itemSelected = null;
            }
        }
        else if (popup != null && !popup.isFinished()) {
            popup.handle(keyStroke);

            if (popup.isFinished()) {
                popup = null;
                renderer.setPopup(null);
            }
        }
        else {
            handle(keyStroke);
        }

        syncHighlightedItem();
        renderer.update();
    }

    private void syncHighlightedItem() {
        if (itemSelected instanceof InputHandler inputItem && !inputItem.isFinished()) {
            renderer.setHighlightedItem(itemSelected);
        } else {
            renderer.setHighlightedItem(null);
        }
    }

    @Override
    public void handle(KeyStroke keyStroke) {
        KeyType keyType = keyStroke.keyType();

        switch (keyType) {
            case ESCAPE -> exit();
            case ENTER -> selectItem(cursor.getPosition());
            case ARROW_UP -> moveCursor(-1);
            case ARROW_DOWN -> moveCursor(1);
        }
    }

    @Override
    public boolean isFinished() {
        return isRunning;
    }

    private Position initCursorPosition() {
        final int cursorX = 1;

        for (int itemIndex = 0; itemIndex < menuList.size(); itemIndex++) {
            if (menuList.get(itemIndex) instanceof SelectableItem) {
                return Position.of(cursorX, itemIndex);
            }
        }

        return Position.of(cursorX, 0);
    }

    private void moveCursor(int cursorMovement) {
        if (menuList.isEmpty()) return;

        int x = cursor.getPosition().x();
        int y = cursor.getPosition().y();

        do {
            y += cursorMovement;

            if (y < 0) y = menuList.size() - 1;

            if (y > menuList.size() - 1) y = 0;

            if (y == cursor.getPosition().y()) return;
        }
        while (!(menuList.get(y) instanceof SelectableItem));

        cursor.setPosition(Position.of(x, y));
    }

    private void selectItem(Position cursorPosition) {
        if (!(menuList.get(cursorPosition.y()) instanceof SelectableItem selectableItem)) return;

        selectableItem.selectItem();
        itemSelected = selectableItem;
    }

    public void exit() {
        isRunning = false;
    }

    public static String getVersion() {
        return Versions.MENU_MANAGER;
    }

    @Override
    public void onError(ErrorEvent errorEvent) {
        popup = new Popup(terminal, errorEvent.error());
        renderer.setPopup(popup);
    }
}
