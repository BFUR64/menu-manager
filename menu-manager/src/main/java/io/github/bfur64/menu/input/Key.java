package io.github.bfur64.menu.input;

public class Key {
    public static final Key UP = new Key();
    public static final Key DOWN = new Key();
    public static final Key ENTER = new Key();
    public static final Key ESCAPE = new Key();
    public static final Key UNKNOWN = new Key();


    private final Character character;

    public Key() {
        this(null);
    }

    public Key(Character character) {
        this.character = character;
    }

    public boolean isCharacter() {
        return character != null;
    }

    public Character character() {
        return character;
    }

    public boolean equals(Key key) {
        return this == key;
    }
}
