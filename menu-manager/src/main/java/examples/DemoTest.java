package examples;

import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DemoTest {
    public static void main(String[] args) throws IOException {
        Terminal terminal = TerminalBuilder.builder().build();

        // Get a non-blocking reader
        NonBlockingReader reader = terminal.reader();

        terminal.writer().println(
            "Capabilities:" +
            terminal.getStringCapability(InfoCmp.Capability.key_up) +
            terminal.getStringCapability(InfoCmp.Capability.key_down) +
            terminal.getStringCapability(InfoCmp.Capability.key_enter)
        );

        terminal.writer().println("Type something (program will exit after 10 seconds):");
        terminal.writer().flush();

        // Track start time
        long startTime = System.currentTimeMillis();

        // Run for 10 seconds
        while (System.currentTimeMillis() - startTime < 10000) {
            try {
                // Check if input is available
                if (reader.available() > 0) {
                    // Read a character (non-blocking)
                    int c = reader.read();
                    terminal.writer().println("Read character: " + (char) c);
                    terminal.writer().flush();
                }

                // Simulate background work
                terminal.writer().print(".");
                terminal.writer().flush();
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        terminal.writer().println("\nTime's up!");
        terminal.close();
    }
}
