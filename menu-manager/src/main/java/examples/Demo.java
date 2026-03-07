package examples;

import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.TerminalRenderer;
import io.github.bfur64.menu.item.TextItem;
import io.github.bfur64.menu.item.Item;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        try (
            Terminal terminal = TerminalBuilder.builder().system(true).jna(true).build();
        ) {
            List<Item> items = List.of(
                new TextItem("Start"),
                new TextItem("Options"),
                new TextItem("Exit")
            );

            MenuManager menu = new MenuManager(items);
            menu.run();

            TerminalRenderer renderer = new TerminalRenderer(terminal);
            renderer.render(menu.getDrawCommands());

            Thread.sleep(1000);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
