package io.github.bfur64.menu;

import io.github.bfur64.menu.event.*;
import io.github.bfur64.menu.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public final class ItemStack {
    private final List<List<Item>> itemStack = new ArrayList<>();

    private final Event event;

    public ItemStack(Event event) {
        this.event = event;

        event.subscribe(MenuExitEvent.class, e -> removeFromStack());
    }

    public void addToStack(List<Item> itemList) {
        for (Item item : itemList) {
            if (item instanceof EventBusAware eventItem) {
                eventItem.setEventBus(event);
            }
        }

        itemStack.addLast(itemList);

        event.publish(new MenuEnterEvent(itemList));
    }

    private void removeFromStack() {
        if (itemStack.size() <= 1) {
            event.publish(new ExitEvent());
            return;
        }

        itemStack.removeLast();
        event.publish(new MenuReturnEvent(itemStack.getLast()));
    }
}
