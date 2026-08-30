package io.github.bfur64.menu;

import io.github.bfur64.Versions;
import io.github.bfur64.menu.event.*;
import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.item.ButtonItem;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

@NullMarked
public final class MenuManager implements InputHandler {
    private static final long NS_PER_FRAME = 1_000_000_000L / 60;
    private static final long PARK_THRESHOLD = 2_000_000L;
    private static final long PARK_MARGIN = 500_000L;

    private final Terminal terminal;

    private final MenuCursor menuCursor;
    private final MenuRenderer menuRenderer;

    private final Event event = new Event();

    private boolean isRunning = true;

    private List<Item> itemList = List.of();

    private @Nullable Item itemSelected = null;
    private final AtomicBoolean itemReady = new AtomicBoolean(false);

    private @Nullable PopupManager popup;

    private int cursorPosition = -1;

    public MenuManager(Terminal terminal, List<Item> itemList) {
        this.terminal = terminal;

        ItemStack itemStack = new ItemStack(event);
        this.menuCursor = new MenuCursor(event);
        this.menuRenderer = new MenuRenderer(terminal, event);

        new PopupManager(terminal, event);

        event.subscribe(MenuEnterEvent.class, e -> this.itemList = e.itemList());
        event.subscribe(MenuReturnEvent.class, e -> this.itemList = e.itemList());

        event.subscribe(ItemSelectEvent.class, e -> itemSelected = e.itemSelected());
        event.subscribe(ItemDeselectEvent.class, e -> itemSelected = null);

        event.subscribe(CursorInitializedEvent.class, e -> cursorPosition = e.cursorPosition());
        event.subscribe(CursorChangeEvent.class, e -> cursorPosition = e.cursorPosition());

        event.subscribe(PopupChangeEvent.class, e -> popup = e.popup());

        event.subscribe(ExitEvent.class, e -> isRunning = false);

        event.subscribe(ItemActionReadyEvent.class, e -> itemReady.set(true));

        itemStack.addToStack(itemList);
    }

    public void start() {
        while (isRunning) {
            long frameStart = System.nanoTime();

            update();

            long deadline = frameStart + NS_PER_FRAME;

            while (true) {
                long now = System.nanoTime();
                long remaining = deadline - now;

                if (remaining <= 0) {
                    break;
                }

                if (remaining > PARK_THRESHOLD) {
                    LockSupport.parkNanos(remaining - PARK_MARGIN);
                }
                else {
                    Thread.onSpinWait();
                }
            }
        }
    }

    private void update() {
        KeyStroke keyStroke = terminal.poll();

        if (keyStroke != null) {
            if (itemSelected instanceof InputHandler inputItem) {
                inputItem.handle(keyStroke);
            }
            else if (popup != null) {
                popup.handle(keyStroke);
            }
            else {
                handle(keyStroke);
            }
        }

        if (itemReady.compareAndSet(true, false)) {
            if (itemSelected instanceof ButtonItem buttonItem) {
                List<Item> itemList = buttonItem.runSelected();

                event.publish(new AddToStackRequest(itemList));
                event.publish(new ItemDeselectEvent());
            }
        }

        menuRenderer.render();
    }

    @Override
    public void handle(KeyStroke keyStroke) {
        if (keyStroke.equals(MenuConfig.menuExitKey.get()) || keyStroke.equals(MenuConfig.menuExitAltKey.get())) {
            event.publish(new MenuExitEvent());
        }
        else if (keyStroke.equals(MenuConfig.menuEnterKey.get()) || keyStroke.equals(MenuConfig.menuEnterAltKey.get())) {
            selectItem();
        }
        else if (keyStroke.equals(MenuConfig.cursorUpKey.get())) {
            menuCursor.moveCursor(-1);
        }
        else if (keyStroke.equals(MenuConfig.cursorDownKey.get())) {
            menuCursor.moveCursor(1);
        }
    }

    private void selectItem() {
        if (cursorPosition < 0 || itemList.isEmpty()) return;

        if (this.itemList.get(cursorPosition) instanceof SelectableItem selectableItem) {
            selectableItem.selectItem();
        }
    }

    public static String getVersion() {
        return Versions.MENU_MANAGER;
    }

    public Event getEvent() {
        return event;
    }
}
