package com.teic.menu.item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor.ANSI;
import com.googlecode.lanterna.TextColor.Indexed;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.Terminal;
import com.teic.menu.MenuContext;

public class EditableItem extends Item {
    private final Supplier<Double> get;
    private final Function<Double, Boolean> set;
    private final String separator = " = ";

    public EditableItem(
            String label,
            Supplier<Double> get,
            Function<Double, Boolean> set
    ) {
        super(label, true);
        this.get = get;
        this.set = set;
    }

    public Double getValue() { return get.get(); }

    public boolean setValue(Double value) {
        return set.apply(value);
    }

    public String getSeparator() { return separator; }

    @Override
    public String getDisplayName() {
        return label + separator + getValue();
    }

    @Override
    public boolean onSelect(MenuContext mainContext)
            throws IOException {
        Terminal terminal = mainContext.terminal();

        TextGraphics textGraphics = mainContext
                .textGraphics();

        int valueLength = ("" + getValue()).length();
        int nameOffset = mainContext
                .colPos()
                + label.length()
                + separator.length();

        textGraphics.setBackgroundColor(ANSI.WHITE);
        textGraphics.setForegroundColor(ANSI.BLACK);

        textGraphics.putString(
                mainContext.colPos(),
                mainContext.rowPos(),
                label
        );

        textGraphics.setBackgroundColor(ANSI.DEFAULT);
        textGraphics.setForegroundColor(ANSI.DEFAULT);

        for (int i = 0; i < valueLength; i++) {
            textGraphics.putString(
                    nameOffset + i,
                    mainContext.rowPos(),
                    " "
            );
        }

        int cursorPos = nameOffset;

        List<Character> charInp = new ArrayList<>();
        StringBuilder sBuilder = new StringBuilder();

        loop:
        while (true) {
            terminal.flush();

            KeyStroke keyStroke = terminal.readInput();
            KeyType keyType = keyStroke.getKeyType();
            Character character = null;

            switch (keyType) {
                case Escape -> {
                    break loop;
                }
                case Character -> {
                    character = keyStroke.getCharacter();

                    textGraphics.putString(
                            cursorPos,
                            mainContext.rowPos(),
                            Character.toString(character)
                    );

                    cursorPos++;

                    charInp.add(character);
                }
                case Backspace -> {
                    character = ' ';
                    cursorPos--;

                    if (cursorPos < nameOffset) {
                        cursorPos = nameOffset;
                    }

                    textGraphics.putString(
                            cursorPos,
                            mainContext.rowPos(),
                            Character.toString(character)
                    );

                    if (!charInp.isEmpty()) {
                        charInp.removeLast();
                    }
                }
                case Enter -> {
                    for (Character ch : charInp) {
                        sBuilder.append(ch);
                    }

                    String userInp = sBuilder.toString();

                    try {
                        double doubleInp =
                                Double.parseDouble(userInp);

                        if (setValue(doubleInp)) {

                        }
                        else {
                            throwUserError(
                                    mainContext,
                                    nameOffset
                            );
                        }
                    }
                    catch (NumberFormatException e) {
                        throwUserError(
                                mainContext,
                                nameOffset
                        );
                    }

                    break loop;
                }
                default -> {}
            }
        }

        return true;
    }

    private void throwUserError(
            MenuContext mc,
            int nameOffset
    ) throws IOException {
        Terminal tm = mc.terminal();
        TextGraphics tg = mc.textGraphics();

        tg.enableModifiers(SGR.UNDERLINE);

        tg.setForegroundColor(Indexed
                .fromRGB(255, 70, 70));

        tg.putString(
                nameOffset,
                mc.rowPos(),
                "Invalid Value"
        );

        tg.disableModifiers(SGR.UNDERLINE);
        tg.setForegroundColor(ANSI.DEFAULT);

        tm.readInput();
    }
}
