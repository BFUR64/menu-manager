package io.github.bfur64.menu;

import io.github.bfur64.menu.input.KeyHit;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.render.Draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuManager {
    private boolean isFinished = false;

    private final List<Draw> draw = new ArrayList<>();
    private final List<Item> menuList;

    private int listIndex = 0;
    private int prevListIndex = listIndex;

    public MenuManager(List<Item> menuList) {
        this.menuList = menuList;
    }

    public void update(KeyHit keyHit) {
        draw.clear();

        drawMenu();
        drawCursor(keyHit);
    }

    public void update() {
        update(KeyHit.UNKNOWN);
    }

    private void drawMenu() {
        for (int i = 0; i < menuList.size(); i++) {
            Draw drawCommand = new Draw(3, i, menuList.get(i).getDisplayName());

            draw.add(drawCommand);
        }
    }

    private void drawCursor(KeyHit keyHit) {
        switch (keyHit) {
            case ESCAPE -> {
                isFinished = true;
            }

            case UP -> {
                prevListIndex = listIndex;

                do {
                    listIndex--;

                    if (listIndex < 0) {
                        listIndex = menuList.size() - 1;
                    }
                }
                while (!menuList.get(listIndex).isSelectable());
            }

            case DOWN -> {
                prevListIndex = listIndex;

                do {
                    listIndex++;

                    if (listIndex > menuList.size() - 1) {
                        listIndex = 0;
                    }
                }
                while (!menuList.get(listIndex).isSelectable());
            }

            case ENTER -> {
                MenuContext menuContext =
                    new MenuContext(
                        draw,
                        3,
                        listIndex + 1
                );

                Item menuItem = menuList.get(listIndex);

                menuItem.selectItem(menuContext);

                if (menuItem.exitRequested()) {
                    isFinished = true;
                }

                update();
            }
        }

        draw.add(new Draw(0, prevListIndex, " "));
        draw.add(new Draw(0, listIndex, ">"));
    }

    public List<Draw> getDrawList() {
        return Collections.unmodifiableList(draw);
    }

    public boolean isFinished() {
        return isFinished;
    }
}
