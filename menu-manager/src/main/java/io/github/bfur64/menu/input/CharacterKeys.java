package io.github.bfur64.menu.input;

import java.util.HashMap;
import java.util.Map;

public class CharacterKeys {
    public static final Map<Character, Key> characterKeyHashMap;

    static {
        characterKeyHashMap = new HashMap<>();

        // 0 -> 9, A -> Z, a -> z
        for (char c = '0'; c <= 'z'; c++) {
            if (Character.isLetterOrDigit(c)) {
                characterKeyHashMap.put(c, new Key(c));
            }
        }
    }
}
