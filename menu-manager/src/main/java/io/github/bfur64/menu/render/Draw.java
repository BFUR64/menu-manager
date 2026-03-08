package io.github.bfur64.menu.render;

public record Draw(int x, int y, String out, Style fg, Style bg) {
    public Draw(int x, int y, String out) {
        this(x, y, out, new Style(), new Style());
    }
}
