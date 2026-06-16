package io.github.bfur64.menu.input;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record KeyAction(KeyStroke keyStroke, Runnable action) {
    public KeyAction(KeyType keyType, Runnable action) {
        this(new KeyStroke(keyType), action);
    }
}
