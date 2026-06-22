package io.github.bfur64.menu.item;

import io.github.bfur64.menu.utils.ExitListener;
import io.github.bfur64.menu.utils.ExitObservable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class ActionItem extends SelectableItem implements ExitObservable {
    private final Runnable action;
    private final boolean exitAfter;
    private @Nullable ExitListener exitListener;

    public ActionItem(String name, Runnable action) {
        this(name, action, false);
    }

    public ActionItem(String name, boolean exitAfter) {
        this(name, () -> {}, exitAfter);
    }

    public ActionItem(String name, Runnable action, boolean exitAfter) {
        super(name);
        this.action = action;
        this.exitAfter = exitAfter;
    }

    @Override
    public void selectItem() {
       action.run();
       if (exitAfter && exitListener != null) exitListener.exit();
    }

    @Override
    public void setExitListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }
}
