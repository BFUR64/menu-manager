package io.github.bfur64.menu.item;

import io.github.bfur64.menu.Event;
import io.github.bfur64.menu.event.AddToStackRequest;
import io.github.bfur64.menu.event.EventBusAware;
import io.github.bfur64.menu.event.ItemDeselectEvent;
import io.github.bfur64.menu.event.ItemSelectEvent;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@NullMarked
public class ButtonItem extends SelectableItem implements EventBusAware {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread thread = new Thread(r, "button-item-scheduler");
        thread.setDaemon(true);
        return thread;
    });

    protected @Nullable Event event;

    protected ButtonItem(String name) {
        super(name);
    }

    @Override
    public void selectItem() {
        if (event != null) {
            event.publish(new ItemSelectEvent(this));
        }

        scheduler.schedule(
            () -> {
                if (event != null) {
                    List<Item> result = runSelected();
                    event.publish(new ItemDeselectEvent());
                    event.publish(new AddToStackRequest(result));
                }
            },
            50,
            TimeUnit.MILLISECONDS
        );
    }

    protected @Nullable List<Item> runSelected() {
        return null;
    }

    @Override
    public void setEventBus(Event event) {
        this.event = event;
    }
}
