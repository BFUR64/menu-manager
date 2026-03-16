package io.github.bfur64.menu.input;

import java.util.HashMap;
import java.util.Map;

public class Key {
    public static final Key UP = new Key();
    public static final Key DOWN = new Key();
    public static final Key ENTER = new Key();
    public static final Key ESCAPE = new Key();
    public static final Key UNKNOWN = new Key();

    private static final Map<Character, Key> characters;

    private final Character character;

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

    private Key(Character character) {
        this.character = character;
    }

    private Key() {
        this(null);
    }

    public boolean isCharacter() {
        return character != null;
    }
}
