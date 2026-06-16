package examples;

import io.github.bfur64.menu.MenuManager;
import io.github.bfur64.menu.Property;
import io.github.bfur64.menu.item.ActionItem;
import io.github.bfur64.menu.item.display.DynamicText;
import io.github.bfur64.menu.item.display.LineBreak;
import io.github.bfur64.menu.item.display.StaticText;
import io.github.bfur64.menu.item.input.InputItem;
import io.github.bfur64.menu.item.input.KeyInputItem;
import io.github.bfur64.menu.item.input.ToggleItem;
import io.github.bfur64.terminal.BufferedTerminal;
import io.github.bfur64.terminal.Terminal;
import io.github.bfur64.terminal.input.KeyStroke;
import io.github.bfur64.terminal.input.KeyType;
import io.github.bfur64.terminal.interfaces.TerminalBackend;

import java.util.Arrays;
import java.util.List;

public class DemoEditable {
    public static void main(String[] args) {
        try (TerminalBackend terminal = BufferedTerminal.auto()) {
            terminal.start();

            MenuManager menu = new MenuManager(terminal, List.of(
                new LineBreak(),
                new StaticText("<< Demo Editable >>"),
                new LineBreak(),
                new StaticText("| Static Text |"),
                new LineBreak(),
                new StaticText(MenuManager.getVersion()),
                new StaticText(Terminal.getLibraryInfo()),
                new StaticText(terminal.getTerminalInfo()),
                new LineBreak(),
                new StaticText("| Dynamic Text |"),
                new LineBreak(),
                new DynamicText<>("Col: ", terminal::getXSize),
                new DynamicText<>("Row: ", terminal::getYSize),
                new LineBreak(),
                new ActionItem("[ Input Test ]", () -> inputTest(terminal)),
                new LineBreak(),
                new KeyInputItem("Key", ": ", Config.keyStrokeProperty),
                new LineBreak(),
                new ActionItem("[ Exit ]", true)
            ));
            menu.start();
        }
        catch (Throwable e) {
            System.out.println("A Throwable got caught in MenuManager!");
            System.out.println(e.getLocalizedMessage() + " : " + Arrays.toString(e.getStackTrace()));
        }
        finally {
            System.exit(0);
        }
    }

    private static void inputTest(TerminalBackend terminal) {
        MenuManager menu = new MenuManager(terminal, List.of(
            new LineBreak(),
            new StaticText("<< Input Test >>"),
            new LineBreak(),
            new InputItem<>("Name", ": ", Config.name),
            new InputItem<>("Age", Config.age),
            new InputItem<>("Allowance", " >> ", Config.allowance , "Philippine Pesos"),
            new InputItem<>("NaN", Config.nan, "nan"),
            new LineBreak(),
            new ActionItem("[ Reset Fields ]", Config::resetFields),
            new ToggleItem("Value Switch", Config.switchFields),
            new LineBreak(),
            new ActionItem("[ Return ]", true)
        ));
        menu.start();
    }

    private static class Config {
        // ------ Default Usage ------
        // Custom Error Messages
        public static Property<String> name = Property.of("")
            .require(n -> !n.isEmpty(), "Name cannot be blank")
            .require(n -> !n.equalsIgnoreCase("terrance"), "Name cannot be Terrance")
            .require(n -> !n.equalsIgnoreCase("drew"), "Name cannot be Drew")
            .parser(String::toString)
            .build();

        // Default Error Messages
        public static Property<Integer> age = Property.of(0)
            .require(a -> a >= 18)
            .require(a -> a <= 50)
            .parser(Integer::parseInt)
            .build();

        // `Require` not needed
        public static Property<Double> allowance = Property.of(0D)
            .parser(Double::parseDouble)
            .build();

        public static Property<String> nan = Property.of("")
            .parser(String::toString)
            .build();

        // --- Custom setters and getters ---
        private static boolean fieldsSwitched;

        private static void switchFields(boolean bool) {
            fieldsSwitched = bool;

            if (fieldsSwitched) {
                switch1();
                return;
            }

            switch2();
        }

        private static void switch1() {
            name.set("Terrance");
            age.set(50);
            allowance.set(-25D);
            nan.set("LMAO");
        }

        private static void switch2() {
            name.set("Andrew");
            age.set(18);
            allowance.set(5000D);
            nan.set("NaN");
        }

        private static boolean isFieldsSwitched() {
            return fieldsSwitched;
        }

        // `Parser` not needed
        public static Property<Boolean> switchFields = Property.of(false)
            .setter(Config::switchFields)
            .getter(Config::isFieldsSwitched)
            .build();
        // ------

        // First-class KeyStroke support
        public static Property<KeyStroke> keyStrokeProperty = Property.of(new KeyStroke(KeyType.UNKNOWN))
            .build();

        private static void resetFields() {
            name.set("");
            age.set(0);
            allowance.set(0D);
            nan.set("");
        }
    }
}
