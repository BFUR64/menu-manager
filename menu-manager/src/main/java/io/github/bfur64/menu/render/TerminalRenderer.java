package io.github.bfur64.menu.render;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.util.List;

public class TerminalRenderer {
    private final Terminal terminal;

    public TerminalRenderer(Terminal terminal) {
        this.terminal = terminal;
    }

    public void render(List<Draw> drawCommands) {
        terminal.puts(Capability.clear_screen);

        for (Draw drawCommand : drawCommands) {
            terminal.puts(Capability.cursor_address, drawCommand.y(), drawCommand.x());
            terminal.writer().print(drawCommand.out());
        }

        terminal.flush();
    }
}
