package io.github.bfur64.menu;

import io.github.bfur64.menu.utils.Position;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class MenuCursor {
    private Position position;
    private String cursorSymbol;

    public MenuCursor(Position position, String cursorSymbol) {
        this.position = position;
        this.cursorSymbol = cursorSymbol;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getCursorSymbol() {
        return cursorSymbol;
    }

    public void setCursorSymbol(String cursorSymbol) {
        this.cursorSymbol = cursorSymbol;
    }
}
