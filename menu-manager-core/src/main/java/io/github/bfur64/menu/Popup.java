package io.github.bfur64.menu;

import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.utils.Position;
import io.github.bfur64.menu.utils.Size;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalBackend;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class Popup implements InputHandler {
    private final TerminalBackend terminal;
    private final String text;
    private final Position position;
    private final Size size;

    private boolean isFinished;

    public Popup(TerminalBackend terminal, String text) {
        this.terminal = terminal;
        this.text = text;

        int padding = 11;
        int fixedHeight = 7;
        this.size = Size.of(text.length() + padding, fixedHeight);


        this.position = Position.of((terminal.getXSize() - (size.x() + 1)) / 2, (terminal.getYSize() - (size.y() + 1)) / 2);
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

        drawCenteredString(position.y() + 2, text);

        terminal.setBackgroundColor(255, 255, 255);
        terminal.setForegroundColor(0, 0, 0);
        drawCenteredString(position.y() + 5, "  OK  ");
        terminal.resetColorAndStyle();
    }

    private void clearBoxContent(int x, int y, int sizeXOffset, int sizeYOffset) {
        for (int posX = x + 1; posX < sizeXOffset; posX++) {
            for (int posY = y + 1; posY < sizeYOffset; posY++) {
                terminal.put(posX, posY, " ");
            }
        }
    }

    public void drawCenteredString(int y, String out) {
        int innerWidth = size.x() - 1;
        terminal.put(position.x() + 1 + (innerWidth - out.length()) / 2, y, out);
    }

    @Override
    public void handle(KeyStroke keyStroke) {
        if (keyStroke.keyType() == KeyType.ENTER) {
            isFinished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
