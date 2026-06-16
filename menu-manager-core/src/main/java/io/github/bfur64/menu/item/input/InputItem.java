package io.github.bfur64.menu.item.input;

import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.Selectable;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.menu.utils.ErrorEvent;
import io.github.bfur64.menu.Property;
import io.github.bfur64.menu.utils.ErrorListener;
import io.github.bfur64.menu.utils.ErrorObservable;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class InputItem<T> extends SelectableItem implements InputHandler, ErrorObservable {
    private final String separator;
    protected final Property<T> property;
    private final String suffix;
    protected @Nullable ErrorListener errorListener;

    protected String value;
    protected boolean isFinished = true;

    public InputItem(String name, Property<T> property) {
        this(name, " = ", property);
    }

    public InputItem(String name, String separator, Property<T> property) {
        this(name, separator, property, "");
    }

    public InputItem(String name, Property<T> property, String suffix) {
        this(name, " = ", property, suffix);
    }

    public InputItem(String name, String separator, Property<T> property, String suffix) {
        super(name);
        this.separator = separator;
        this.property = property;
        this.suffix = suffix;
        value = property.get().toString();
    }

    @Override
    public String getDisplayName() {
        if (isFinished) {
            value = property.get().toString();
        }

        return name + separator + value + " " + suffix;
    }

    @Override
    public void selectItem() {
        isFinished = false;
        value = "";
    }

    @Override
    public void handle(KeyStroke keyStroke) {
        KeyType keyType = keyStroke.keyType();

        switch (keyType) {
            case ESCAPE -> {
                value = property.get().toString();
                isFinished = true;
            }
            case CHARACTER -> value += keyStroke.character();
            case BACKSPACE -> {
                if (!value.isEmpty()) {
                    value = value.substring(0, value.length() - 1);
                }
            }
            case ENTER -> {
                try {
                    if (property.isValidFromString(value)) {
                        property.setFromString(value);
                        isFinished = true;
                        break;
                    }

                    if (errorListener != null && property.getLatestError() != null) {
                        errorListener.onError(new ErrorEvent(property.getLatestError()));
                    }

                    value = property.get().toString();
                }
                catch (IllegalArgumentException e) {
                    if (errorListener != null) {
                        errorListener.onError(new ErrorEvent("Unexpected Input"));
                    }

                    value = property.get().toString();
                }

                isFinished = true;
            }
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }

    @Override
    public void setErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }
}
