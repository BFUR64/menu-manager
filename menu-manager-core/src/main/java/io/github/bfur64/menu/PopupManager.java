package io.github.bfur64.menu;

import io.github.bfur64.menu.event.ErrorEvent;
import io.github.bfur64.menu.event.PopupChangeEvent;
import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.utils.Position;
import io.github.bfur64.menu.utils.Size;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.output.SGR;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class PopupManager implements InputHandler {
    private final Terminal terminal;
    private final Event event;

    private String error = "";
    private Size size = Size.of(-1, -1);
    private Position position = Position.of(-1, -1);

    public PopupManager(Terminal terminal, Event event) {
        this.terminal = terminal;
        this.event = event;

        int padding = 11;
        int fixedHeight = 7;

        event.subscribe(ErrorEvent.class, e -> {
            error = e.error();
            this.size = Size.of(error.length() + padding, fixedHeight);
            this.position = Position.of((terminal.xSize() - (size.x() + 1)) / 2, (terminal.ySize() - (size.y() + 1)) / 2);

            event.publish(new PopupChangeEvent(this));
        });
    }

    public void draw() {
        int x = position.x();
        int y = position.y();
        int sizeXOffset = position.x() + size.x();
        int sizeYOffset = position.y() + size.y();

        terminal.put(x, y, "╔");
        terminal.put(x, sizeYOffset, "╚");

        terminal.put(sizeXOffset, y, "╗");
        terminal.put(sizeXOffset, sizeYOffset, "╝");

        for (int posX = x + 1; posX < sizeXOffset; posX++) {
            terminal.put(posX, y, "═");
            terminal.put(posX, sizeYOffset, "═");
        }

        for (int posY = y + 1; posY < sizeYOffset; posY++) {
            terminal.put(x, posY, "║");
            terminal.put(sizeXOffset, posY, "║");
        }

        clearBoxContent(x, y, sizeXOffset, sizeYOffset);

        drawCenteredString(position.y() + 2, error);

        terminal.onSGR(SGR.REVERSE);
        drawCenteredString(position.y() + 5, "  OK  ");
        terminal.offSGR(SGR.REVERSE);
    }

    private void clearBoxContent(int x, int y, int sizeXOffset, int sizeYOffset) {
        for (int posX = x + 1; posX < sizeXOffset; posX++) {
            for (int posY = y + 1; posY < sizeYOffset; posY++) {
                terminal.put(posX, posY, " ");
            }
        }
    }

    private void drawCenteredString(int y, String out) {
        int innerWidth = size.x() - 1;
        terminal.put(position.x() + 1 + (innerWidth - out.length()) / 2, y, out);
    }

    @Override
    public void handle(KeyStroke keyStroke) {
        if (keyStroke.keyType() == KeyType.ENTER || keyStroke.keyType() == KeyType.ESCAPE) {
            event.publish(new PopupChangeEvent(null));
        }
    }
}
