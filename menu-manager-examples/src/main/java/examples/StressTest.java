package examples;

import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.interfaces.TerminalRuntime;

import java.util.List;

public class StressTest {
    private final Terminal terminal;

    public static void main(String[] args) throws Exception {
        try (TerminalRuntime runtime = Terminal.builder().auto().build()) {
            Terminal terminal = runtime.terminal();

            StressTest test = new StressTest(terminal);

            MenuManager menu = new MenuManager(terminal, List.of(
                    new ActionItem("[ Test 1 ]", test::test1),
                    new ActionItem("[ Test 2 ]", test::test2),
                    new ActionItem("[ Test 3 ]", test::test3),
                    new ActionItem("[ Test 4 ]", test::test4)
            ));
            menu.start();
        }
    }

    public StressTest(Terminal terminal) {
        this.terminal = terminal;
    }

    private void test1() {
        MenuManager menu = new MenuManager(terminal, List.of(
                new StaticText("Text Only"),
                new StaticText("Text Only"),
                new StaticText("Text Only"),
                new StaticText("Text Only")
        ));
        menu.start();
    }

    private void test2() {
        MenuManager menu = new MenuManager(terminal, List.of(
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
        ));
        menu.start();
    }

    private void test3() {
        MenuManager menu = new MenuManager(terminal, List.of(
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
        ));
        menu.start();
    }

    private void test4() {
        MenuManager menu = new MenuManager(terminal, List.of(
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
        ));
        menu.start();
    }
}
