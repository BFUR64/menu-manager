package io.github.bfur64.menu.item;

import io.github.bfur64.menu.utils.ExitListener;
import io.github.bfur64.menu.utils.ExitObservable;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class ActionItem extends SelectableItem implements ExitObservable {
    private final RunnableAction action;
    private final boolean exitAfter;
    private @Nullable ExitListener exitListener;

    public ActionItem(String name, Runnable action) {
        this(name, () -> {
            action.run();
            return null;
        },
        false);
    }

    public ActionItem(String name, RunnableAction action) {
        this(name, action, false);
    }

    public ActionItem(String name, boolean exitAfter) {
        this(name, () -> null, exitAfter);
    }

    public ActionItem(String name, RunnableAction action, boolean exitAfter) {
        super(name);
        this.action = action;
        this.exitAfter = exitAfter;
    }

    @Override
    public @Nullable List<Item> selectItem() {
       List<Item> itemList = action.run();
       if (exitAfter && exitListener != null) exitListener.exit();

       return itemList;
    }

    @Override
    public void setExitListener(ExitListener exitListener) {
        this.exitListener = exitListener;
    }
}
