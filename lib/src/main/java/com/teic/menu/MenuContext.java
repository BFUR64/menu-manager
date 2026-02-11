package com.teic.menu;

import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.terminal.Terminal;

public record MenuContext (
        Terminal terminal,
        TextGraphics textGraphics,
        int colPos,
        int rowPos
) {}
