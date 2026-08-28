package examples;

import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.Item;
import io.github.bfur64.menu.item.ListItem;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

import java.util.List;

public class StressTest {
    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            StressTest test = new StressTest();

            MenuManager menu = new MenuManager(terminal, List.of(
                new ListItem("[ Test 1 ]", test::test1),
                new ListItem("[ Test 2 ]", test::test2),
                new ListItem("[ Test 3 ]", test::test3),
                new ListItem("[ Test 4 ]", test::test4)
            ));
            menu.start();
        }
    }

    private List<Item> test1() {
        return List.of(
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only")
        );
    }

    private List<Item> test2() {
        return List.of(
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new ActionItem("Action!", false),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only")
        );
    }

    private List<Item> test3() {
        return List.of(
            new ActionItem("Action!", false),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new ActionItem("Action!", false)
        );
    }

    private List<Item> test4() {
        return List.of(
            new ActionItem("Action!", false),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new ActionItem("Action!", false),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new StaticText("Text Only"),
            new ActionItem("Action!", false)
        );
    }
}
