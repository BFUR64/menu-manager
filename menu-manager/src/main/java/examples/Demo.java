package examples;

import io.github.bfur64.menu.KeyHit;
import io.github.bfur64.menu.KeyReader;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.render.TerminalRenderer;
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
                new ActionItem("Lol", () -> {return true;} ),
                new ActionItem("Lol", () -> {return true;} ),
                new TextItem("Start"),
                new TextItem("Options"),
                new TextItem("Exit")
            );

            KeyReader reader = new KeyReader(terminal);
            MenuManager menu = new MenuManager(items);
            TerminalRenderer renderer = new TerminalRenderer(terminal);

            menu.update(KeyHit.UNKNOWN);
            renderer.render(menu.getDrawCommands());

            while (true) {
                KeyHit hit = reader.readKeyPress();
                menu.update(hit);

                renderer.render(menu.getDrawCommands());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
