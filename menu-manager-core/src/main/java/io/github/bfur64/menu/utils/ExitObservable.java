package io.github.bfur64.menu.utils;

import org.jspecify.annotations.NullMarked;

@NullMarked
public interface ExitObservable {
    void setExitListener(ExitListener exitListener);
}
