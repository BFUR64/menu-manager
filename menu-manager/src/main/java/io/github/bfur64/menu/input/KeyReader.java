package io.github.bfur64.menu.input;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

public class KeyReader {
    private final KeyMap<KeyHit> keyMap = new KeyMap<>();

    private final BindingReader bindingReader;

    public KeyReader(Terminal terminal) {
        bindingReader = new BindingReader(terminal.reader());

        keyMap.bind(KeyHit.UP, KeyMap.key(terminal, Capability.key_up));
        keyMap.bind(KeyHit.DOWN, KeyMap.key(terminal, Capability.key_down));

        keyMap.bind(KeyHit.ENTER, KeyMap.key(terminal, Capability.key_enter));
        keyMap.bind(KeyHit.ENTER, "\r");
        keyMap.bind(KeyHit.ENTER, "\n");

        keyMap.bind(KeyHit.ESCAPE, KeyMap.esc());

        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                keyMap.bind(KeyHit.CHARACTER, String.valueOf(c));
            }
        }

        keyMap.setNomatch(KeyHit.UNKNOWN);
    }

    public KeyHit readKeyPress() {
        return bindingReader.readBinding(keyMap);
    }
}
