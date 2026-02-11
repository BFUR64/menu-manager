package com.teic.menu.item;

import java.io.IOException;

@FunctionalInterface
public interface Actionable {
    public boolean run() throws IOException;
}
