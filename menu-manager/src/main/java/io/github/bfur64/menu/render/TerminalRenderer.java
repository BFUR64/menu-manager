package io.github.bfur64.menu.render;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
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

            Style fg = drawCommand.fg();
            Style bg = drawCommand.bg();

            AttributedStyle style = AttributedStyle.DEFAULT;

            if (fg != null) {
                style = style.foreground(fg.r(), fg.g(), fg.b());
            }

            if (bg != null) {
                style = style.background(bg.r(), bg.g(), bg.b());
            }

            AttributedString str = new AttributedString(
                drawCommand.out(),
                style
            );

            terminal.writer().print(str.toAnsi(terminal));
        }

        terminal.flush();
    }
}
