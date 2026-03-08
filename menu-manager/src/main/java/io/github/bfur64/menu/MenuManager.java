package io.github.bfur64.menu;

import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.render.Draw;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuManager {
    private final List<Draw> drawCommands = new ArrayList<>();
    private final List<Item> menuList;

    private int listIndex = 0;
    private int prevListIndex = listIndex;

    public MenuManager(List<Item> menuList) {
        this.menuList = menuList;
    }

    public void update(KeyHit hit) {
        drawMenu();
        drawCursor(hit);
    }

    private void drawMenu() {
        for (int i = 0; i < menuList.size(); i++) {
            Draw drawCommand = new Draw(4, i + 1, menuList.get(i).getDisplayName());

            drawCommands.add(drawCommand);
        }
    }

    private void drawCursor(KeyHit hit) {
//        while (!menuList.get(listIndex).selectable() && listIndex < menuList.size() - 1) {
//            listIndex++;
//        }

        drawCommands.add(new Draw(2, prevListIndex + 1, " "));
        drawCommands.add(new Draw(2, listIndex + 1, ">"));

        switch (hit) {
            case UP -> {
                prevListIndex = listIndex;

                do {
                    listIndex--;

                    if (listIndex < 0) {
                        listIndex = menuList.size() - 1;
                    }
                }
                while (!menuList.get(listIndex).selectable());
            }

            case DOWN -> {
                prevListIndex = listIndex;

                do {
                    listIndex++;

                    if (listIndex > menuList.size() - 1) {
                        listIndex = 0;
                    }
                } while (!menuList.get(listIndex).selectable());
            }
        }
    }

    public List<Draw> getDrawCommands() {
        return Collections.unmodifiableList(drawCommands);
    }
}
