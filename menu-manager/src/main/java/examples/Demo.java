package examples;

import io.github.bfur64.menu.input.KeyHit;
import io.github.bfur64.menu.input.KeyReader;
import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.render.TerminalRenderer;
import io.github.bfur64.menu.item.TextItem;
import io.github.bfur64.menu.item.Item;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        try (
                Terminal terminal = TerminalBuilder.builder().system(true).jna(true).build();
        ) {
            terminal.puts(Capability.cursor_invisible, true);

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

            menu.update();
            renderer.render(menu.getDrawList());

            // TODO Add menu.isFinished() to indicate the menu wants to exit. Preferably some kind of escape character, but... Escape doesn't seem to work here?
            // TODO Investigate if ESCAPE can be used in reading Key Bindings. Useful for escaping the program
            while (true) {
                KeyHit hit = reader.readKeyPress();
                menu.update(hit);

                renderer.render(menu.getDrawList());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
