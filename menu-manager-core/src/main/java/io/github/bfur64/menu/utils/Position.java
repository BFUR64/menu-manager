package io.github.bfur64.menu.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Position(int x, int y) {
    public static Position of(int x, int y) {
        return new Position(x, y);
    }

    public Position add(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}
