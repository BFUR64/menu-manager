package io.github.bfur64.menu.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class KeyReader {
    private final KeyMap<Key> keyMap = new KeyMap<>();

    private final BindingReader bindingReader;

    public KeyReader(Terminal terminal) {
        bindingReader = new BindingReader(terminal.reader());

        keyMap.bind(Key.UP, KeyMap.key(terminal, Capability.key_up));
        keyMap.bind(Key.DOWN, KeyMap.key(terminal, Capability.key_down));

        keyMap.bind(Key.ENTER, KeyMap.key(terminal, Capability.key_enter));
        keyMap.bind(Key.ENTER, "\r");
        keyMap.bind(Key.ENTER, "\n");

        keyMap.bind(Key.ESCAPE, KeyMap.esc());

        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                keyMap.bind(Key.getKeyFromCharacter(c), String.valueOf(c));
            }
        }

        keyMap.setNomatch(Key.UNKNOWN);
    }

    public Key readKeyPress() {
        return bindingReader.readBinding(keyMap);
    }
}
