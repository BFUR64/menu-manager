package io.github.bfur64.menu;

import io.github.bfur64.Versions;
import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.menu.utils.*;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@NullMarked
public class MenuManager implements InputHandler, ErrorListener, ExitListener {
    private static final KeyStroke UNKNOWN_KEY = new KeyStroke(KeyType.UNKNOWN);
    private static final long NS_PER_FRAME = 1_000_000_000L / 60;

    private final Terminal terminal;
    private final MenuCursor cursor;
    private final MenuRenderer renderer;

    private final List<List<Item>> menuStack = new ArrayList<>();
    private List<Item> currentList;

    private @Nullable Item itemSelected;
    private @Nullable Popup popup;

    private boolean isRunning = true;

    public MenuManager(Terminal terminal, List<Item> menuList) {
        this.terminal = terminal;
        this.menuStack.addLast(menuList);
        this.currentList = menuList;

        initList(menuList);

        cursor = new MenuCursor(initCursorPosition(), ">");

        int itemIndent = cursor.getCursorSymbol().length() + 2;
        renderer = new MenuRenderer(terminal, menuList, cursor, itemIndent);
    }

    public void start() {
        while (isRunning) {
            long frameStart = System.nanoTime();

            // START
            KeyStroke keyStroke = terminal.poll();

            if (keyStroke == null) {
                keyStroke = UNKNOWN_KEY;
            }

            update(keyStroke);
            // END

            long deadline = frameStart + NS_PER_FRAME;
            long now = System.nanoTime();

            while (now < deadline) {
                LockSupport.parkNanos(deadline - now);
                now = System.nanoTime();
            }
        }
    }

    private void initList(List<Item> menuList) {
        for (Item item : menuList) {
            if (item instanceof ErrorObservable observableItem) {
                observableItem.setErrorListener(this);
            }

            if (item instanceof ExitObservable observable) {
                observable.setExitListener(this);
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
        }
        else {
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

        for (int itemIndex = 0; itemIndex < currentList.size(); itemIndex++) {
            if (currentList.get(itemIndex) instanceof SelectableItem) {
                return Position.of(cursorX, itemIndex);
            }
        }

        return Position.of(cursorX, 0);
    }

    private void moveCursor(int cursorMovement) {
        if (currentList.isEmpty()) return;

        int x = cursor.getPosition().x();
        int y = cursor.getPosition().y();

        do {
            y += cursorMovement;

            if (y < 0) y = currentList.size() - 1;

            if (y > currentList.size() - 1) y = 0;

            if (y == cursor.getPosition().y()) return;
        }
        while (!(currentList.get(y) instanceof SelectableItem));

        cursor.setPosition(Position.of(x, y));
    }

    private void selectItem(Position cursorPosition) {
        if (!(currentList.get(cursorPosition.y()) instanceof SelectableItem selectableItem)) return;

        List<Item> itemList = selectableItem.selectItem();

        if (itemList != null) {
            menuStack.addLast(itemList);
            currentList = itemList;
            renderer.replaceItems(itemList);
            cursor.setPosition(initCursorPosition());
            initList(itemList);
            return;
        }

        itemSelected = selectableItem;
    }

    @Override
    public void exit() {
        if (menuStack.size() == 1) {
            isRunning = false;
            return;
        }

        menuStack.removeLast();
        currentList = menuStack.getLast();
        renderer.replaceItems(currentList);
        cursor.setPosition(initCursorPosition());
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
