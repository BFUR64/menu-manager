package io.github.bfur64.menu.event;

import io.github.bfur64.menu.PopupManager;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record PopupChangeEvent(@Nullable PopupManager popup) {}
