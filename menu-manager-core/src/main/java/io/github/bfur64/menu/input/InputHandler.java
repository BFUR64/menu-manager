package io.github.bfur64.menu.input;

import io.github.bfur64.terminal.input.KeyStroke;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface InputHandler {
    void handle(KeyStroke keyStroke);
    boolean isFinished();
}
