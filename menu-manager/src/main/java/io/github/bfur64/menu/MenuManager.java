package io.github.bfur64.menu;

import io.github.bfur64.menu.item.Item;
import org.jline.keymap.KeyMap;
import org.jline.reader.LineReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuManager {
    private final List<Draw> drawCommands = new ArrayList<>();
    private final List<Item> menuList;

    public MenuManager(List<Item> menuList) {
        this.menuList = menuList;
    }

    public void run() {
        drawMenu();
    }

    private void drawMenu() {
        for (int i = 0; i < menuList.size(); i++) {
            Draw drawCommand = new Draw(4, i + 1, menuList.get(i).getDisplayName());

            drawCommands.add(drawCommand);
        }
    }

    private void drawCursor() {
        int listIndex = 0;
        int prevListIndex = listIndex;

        while (!menuList.get(listIndex).selectable()) {
            listIndex++;
        }

        loop:
        while (true) {

        }
    }

    public List<Draw> getDrawCommands() {
        return Collections.unmodifiableList(drawCommands);
    }
}
