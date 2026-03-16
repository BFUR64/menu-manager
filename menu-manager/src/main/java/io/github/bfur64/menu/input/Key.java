package io.github.bfur64.menu.input;

import java.util.HashMap;
import java.util.Map;

public record Key(Character character) {
    public static final Key UP = new Key();
    public static final Key DOWN = new Key();
    public static final Key ENTER = new Key();
    public static final Key ESCAPE = new Key();
    public static final Key UNKNOWN = new Key();

    private static final Map<Character, Key> characters;

    static {
        characters = new HashMap<>();

        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                characters.put(c, new Key(c));
            }
        }
    }

    public static Key getKeyFromCharacter(Character c) {
        return characters.get(c);
    }

    public Key() {
        this(null);
    }

    public boolean isCharacter() {
        return character != null;
    }

    public boolean equals(Key key) {
        return this == key;
    }
}
