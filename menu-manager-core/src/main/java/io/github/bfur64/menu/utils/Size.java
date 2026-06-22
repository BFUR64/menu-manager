package io.github.bfur64.menu.utils;

public record Size(int x, int y) {
    public static Size of(int x, int y) {
        return new Size(x, y);
    }
}
