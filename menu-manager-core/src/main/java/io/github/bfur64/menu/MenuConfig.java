package io.github.bfur64.menu;

import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;

public final class MenuConfig {
    public static final Property<KeyStroke> cursorUpKey = Property.of(new KeyStroke(KeyType.ARROW_UP)).build();
    public static final Property<KeyStroke> cursorDownKey = Property.of(new KeyStroke(KeyType.ARROW_DOWN)).build();

    public static final Property<KeyStroke> menuEnterKey = Property.of(new KeyStroke(KeyType.ENTER)).build();
    public static final Property<KeyStroke> menuEnterAltKey = Property.of(new KeyStroke(' ')).build();

    public static final Property<KeyStroke> menuExitKey = Property.of(new KeyStroke(KeyType.ESCAPE)).build();
    public static final Property<KeyStroke> menuExitAltKey = Property.of(new KeyStroke(KeyType.BACKSPACE)).build();
}
