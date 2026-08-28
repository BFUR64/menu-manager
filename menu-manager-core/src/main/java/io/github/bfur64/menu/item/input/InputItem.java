package io.github.bfur64.menu.item.input;

import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.EventBusAware;
import io.github.bfur64.menu.Property;
import io.github.bfur64.menu.event.ItemSelectChangeEvent;
import io.github.bfur64.menu.input.InputHandler;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.SelectableItem;
import io.github.bfur64.menu.event.ErrorEvent;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
public class InputItem<T> extends SelectableItem implements InputHandler, EventBusAware {
    private final String separator;
    protected final Property<T> property;
    private final String suffix;

    private @Nullable Event event;

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
    public void handle(KeyStroke keyStroke) {
        KeyType keyType = keyStroke.keyType();

        switch (keyType) {
            case ESCAPE -> {
                value = property.get().toString();
                setFinished();
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
                        setFinished();
                        break;
                    }

                    if (event != null && property.getLatestError() != null) {
                        event.publish(new ErrorEvent(property.getLatestError()));
                    }

                    value = property.get().toString();
                }
                catch (IllegalArgumentException e) {
                    if (event != null) {
                        event.publish(new ErrorEvent("Unexpected Input"));
                    }

                    value = property.get().toString();
                }

                setFinished();
            }
        }
    }

    protected void setFinished() {
        if (event != null) {
            event.publish(new ItemSelectChangeEvent(null));
        }

        isFinished = true;
    }

    @Override
    public void setEventBus(Event event) {
        this.event = event;
    }

    @Override
    public @Nullable List<Item> selectItem() {
        isFinished = false;
        value = "";

        return null;
    }
}
