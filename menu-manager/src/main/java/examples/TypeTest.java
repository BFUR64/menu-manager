package examples;

import io.github.bfur64.menu.input.Key;
import io.github.bfur64.menu.input.KeyReader;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TypeTest {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder().build();

        terminal.writer().println("Type Test");
        terminal.writer().flush();

        // Track start time
        long startTime = System.currentTimeMillis();

        KeyReader reader = new KeyReader(terminal);

        // Run for 10 seconds
        while (System.currentTimeMillis() - startTime < 10000) {
            Key hit = reader.readKeyPress();

            String print = "";

            if (hit.equals(Key.UP)) {
                print = "UP";
            }
            else if (hit.equals(Key.DOWN)) {
                print = "DOWN";
            }
            else if (hit.equals(Key.ENTER)) {
                print = "ENTER";
            }
            else if (hit.equals(Key.ESCAPE)) {
                print = "ESCAPE";
            }
            else if (hit.isCharacter()) {
                print = String.valueOf(hit.character());
            }

            terminal.writer().println(print);
            terminal.flush();
        }

        terminal.writer().println("\nTime's up!");
        terminal.close();
    }
}
