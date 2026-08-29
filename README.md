[![License](https://img.shields.io/github/license/bfur64/menu-manager)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://openjdk.org/)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.bfur64/menu-manager)](https://central.sonatype.com/artifact/io.github.bfur64/menu-manager)

<h1 align="center">Menu Manager</h1>

<h3 align="center">A lightweight, composable menu system for terminal applications for Java 21+</h3>

<div align="center">
  <img width="612" height="296" alt="" src="https://github.com/user-attachments/assets/82459d5a-0a77-45b0-9c9e-877d924a65a9" />
</div>

<div align="center">
  <h2>Demo</h2>
  <img width="640" height="476" alt="ezgif-76173d48fdbe0ee6" src="https://github.com/user-attachments/assets/6e174d55-eaf4-4a37-86e0-3ef775cf8496" />
</div>

## Quick Start

```java
Property<Integer> volume = Property.of(50)
    .require(v -> v >= 0 && v <= 100, "Volume must be 0-100")
    .parser(Integer::parseInt)
    .build();

MenuManager menu = new MenuManager(terminal, List.of(
    new StaticText("== Settings =="),
    new InputItem<>("Volume", volume, "%"),
    new ToggleItem("Mute", Property.of(false).build()),
    new ActionItem("[ Save ]", () -> System.out.println("Saved!"))
));
menu.start();
```

## Installation / Running

### Kotline

```kotlin
dependencies {
    implementation("io.github.bfur64:menu-manager:0.9.0")
    implementation("io.github.bfur64:tetrue-terminal:3.2.1")
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>io.github.bfur64</groupId>
        <artifactId>menu-manager</artifactId>
        <version>0.9.0</version>
    </dependency>

    <dependency>
        <groupId>io.github.bfur64</groupId>
        <artifactId>tetrue-terminal</artifactId>
        <version>3.2.2</version>
    </dependency>
</dependencies>
```

## Features

- Declarative terminal menu composition
- Generic mutable `Property<T>` state system
- Built-in validation and parsing pipelines
- Editable menu items with inline error handling
- Dynamic rendering support
- Custom keybind editing
- Zero reflection
- Pure Java

## Usage

### Basic Menu

```java
List<Item> items = List.of(
    new StaticText("== Settings =="),
    new EditableItem<>("Gravity", gravity),
    new ToggleItem("Fullscreen", fullscreen),
    new ActionItem("[ Start Game ]", this::startGame),
    new ActionItem("[ Exit ]", true)
);

MenuManager menu = new MenuManager(terminal, items);
menu.start();
```

### Properties & Validation

`Property<T>` is the core mutable state abstraction used throughout the menu system.
A property can define:
- Current value storage
- Validation rules
- Error messages
- String parsing logic (Optional)
- Custom getters/setters (Optional)

```java
public static Property<Integer> gravity = Property.of(500)
    .require(value -> value >= 50, "Gravity must be at least 50 ms")
    .require(value -> value <= 2000, "Gravity must be less than 2000 ms")
    .parser(Integer::parseInt)
    .build();
```

Editable menu items (e.g. `InputItem`, `KeyInputItem`, `ToggleItem`) automatically use these validators and error messages.

### Custom Setters & Getters

Custom getters and setters allow properties to bind directly to external state instead of using internally managed storage.

Define a class for external state

```java
class Config {
    int fps = 60;

    public int getFps() { ... }
    public void setFps(int fps) { ... }
}
Config config = new Config();
```

Usage
```java
Property<Integer> fps = Property.of(60)
    .getter(config::getFps)
    .setter(config::setFps)
    .parser(Integer::parseInt)
    .build();
```

or direct lambda call

```java
Integer fpsField = 50;
Property<Integer> fps = Property.of(60)
    .getter(() -> fpsField )
    .setter(fps -> {fpsField = fps; })
    .build();
```

### Key Bindings

```java
public static Property<KeyStroke> rotateLeftKey = Property.of(new KeyStroke('q')).build();

public static Property<KeyStroke> rotateRightKey = Property.of(new KeyStroke('e')).build();
```

### Chaining Menus

```java
private void showMainMenu() {
    MenuManager menu = new MenuManager(terminal, List.of(
        new ActionItem("[ Options ]", this::showOptionsMenu),
        new ActionItem("[ Exit ]", true)
    ));
    menu.start();
}

private void showOptionsMenu() {
    MenuManager menu = new MenuManager(terminal, List.of(
        new InputItem<>("Volume", volumeProperty),
        new ActionItem("[ Back ]", true)  // Returns to main menu
    ));
    menu.start();
    // Control returns here after submenu exits
}
```

Menus can stack. Calling `new MenuManager().start()` inside an `ActionItem` creates a submenu.

## Complete Examples

### Game Settings Menu

```java
// 1. Define your config with Properties
class GameConfig {
    static Property<Integer> difficulty = Property.of(5)
        .require(d -> d >= 1 && d <= 10, "Difficulty must be 1-10")
        .parser(Integer::parseInt)
        .build();
    
    static Property<KeyStroke> jumpKey = Property.of(new KeyStroke(' '))
        .build();
    
    static Property<Boolean> soundEnabled = Property.of(true)
        .build();
}

// 2. Build your menu
public static void main(String[] args) throws IOException {
    try (TerminalRuntime terminal = Terminal.builder().auto().build()) {
        Terminal terminal = runtime.terminal();

        MenuManager menu = new MenuManager(terminal, List.of(
            new LineBreak(),
            new StaticText("== Game Settings =="),
            new LineBreak(),
            new InputItem<>("Difficulty", ": ", GameConfig.difficulty),
            new KeyInputItem("Jump Key", GameConfig.jumpKey),
            new ToggleItem("Sound", GameConfig.soundEnabled),
            new LineBreak(),
            new ActionItem("[ Start Game ]", () -> startGame(terminal)),
            new ActionItem("[ Exit ]", true)
        ));

        menu.start();
    }
}

// 3. Use the validated config in your game
private static void startGame(Terminal terminal) {
    int difficulty = GameConfig.difficulty.get();  // Guaranteed valid
    KeyStroke jump = GameConfig.jumpKey.get();

    // Game loop
    while (true) {
        KeyStroke input = terminal.readInput();
        if (input.equals(jump)) {
            player.jump();
        }
    }
}
```

### Error Handling

When a user enters invalid input, Menu Manager:
1. Catches the error from the Property validator
2. Displays the error message in red below the item
3. Prompts "Press Any Key To Continue"
4. Returns to editing without losing the menu state

```java
Property<Integer> age = Property.of(18)
    .require(a -> a >= 18, "Must be 18 or older")
    .require(a -> a <= 100, "Really?")
    .parser(Integer::parseInt)
    .build();

new InputItem<>("Age", age);

// User types "15" and presses Enter:
// → Screen shows: "Must be 18 or older" (in red)
// → Waits for any key press
// → Returns to menu (value unchanged)
```

## API Reference

### Menu Items

| Item Type | Purpose | Constructor Example |
|-----------|---------|---------------------|
| `StaticText` | Non-interactive labels | `new StaticText("== Header ==")` |
| `LineBreak` | Visual spacing | `new LineBreak()` |
| `InputItem<T>` | User-editable values with validation | `new InputItem<>("Name", property)` |
| `InputItem<T>` (with suffix) | Editable with units | `new InputItem<>("Speed", property, "km/h")` |
| `ToggleItem` | Boolean on/off switch | `new ToggleItem("Enabled", boolProperty)` |
| `ActionItem` | Executes callback on select | `new ActionItem("[ Start ]", this::start)` |
| `ActionItem` (exit) | Exits menu after action | `new ActionItem("[ Save ]", this::save, true)` |
| `KeyInputItem` | Keybind editor | `new KeyInputItem("Jump", keyProperty)` |
| `DynamicText<T>` | Auto-updating display | `new DynamicText<>("FPS: ", fps::get)` |

### Item Constructors

```java
// InputItem variants
new InputItem<>("Name", property)
new InputItem<>("Name", ": ", property)  // Custom separator
new InputItem<>("Speed", property, "km/h")  // With suffix
new InputItem<>("Speed", " = ", property, "km/h")  // Both

// ActionItem variants
new ActionItem("[ Start ]", callback)  // Regular action
new ActionItem("[ Exit ]", true)  // Exit menu after
new ActionItem("[ Save ]", this::save, true)  // Both

// DynamicText variants
new DynamicText<>("Value: ", supplier)
new DynamicText<>("Price: ", "$", supplier)  // With prefix
```

### Property Builder

```java
Property.of(initialValue)
    .require(predicate)  // Validation without message (uses default)
    .require(predicate, "Error message")  // With custom error
    .parser(String::parseMethod)  // String → T conversion
    .getter(supplier)  // Read from external state
    .setter(consumer)  // Write to external state
    .build()
```

**Key Methods:**
- `T get()` - Retrieve current value
- `void set(T value)` - Update value (throws if invalid)
- `void setFromString(String stringValue)` - Parse and set (requires `.parser()`)
- `boolean isValid(T value)` - Check without setting
- `boolean isValidFromString(String stringValue)` - Parse and check
- `String getLatestError()` - Get last validation failure message

## How It Works

Menu Manager uses a simple render loop that:

1. Gets user input (if any)
2. Calls `MenuRenderer` to print state changes
3. Loops (with a cap of 60fps) using `LockSupport` until an exit condition

```java
// Simplified internal flow
while (isRunning) {
    // Checks for input
    KeyStroke keyStroke = terminal.pollInput();

    if (keyStroke == null) {
        keyStroke = UNKNOWN_KEY;
    }

    // Delegates updating to `MenuRenderer` via a method call within `MenuManager`
    update(keyStroke);

    // Checks if `MenuManager` should exit based on a boolean method of a `SelectableItem`
    if (itemSelected != null &&
        itemSelected instanceof SelectableItem selectableItem &&
        selectableItem.shouldExit()
    ) {
        exit();
    }
}
```

**Editable Items:**
When you select an `InputItem`, it:
1. Highlights the item name with inverted colors
2. Enters an **inline editing mode** (separate input loop)
3. Validates each Enter press using the Property's rules
4. Shows validation errors in red below the item
5. Returns to the main menu on Escape or successful validation

**Cursor Navigation:**
The cursor automatically skips non-selectable items (like `StaticText` and `LineBreak`), wrapping at boundaries.

**Parser errors:**
If parsing fails (e.g., user types "abc" for an `Integer` property), the default error is `"Unexpected Input"`

## Limitations

- **Single-threaded rendering**: The menu blocks the main thread during `run()`
- **No mouse support**: Keyboard navigation only (arrow keys, Enter, Escape)
- **No scrolling**: All menu items must fit on screen simultaneously
- **Static item lists**: Menu structure is immutable after construction (use `DynamicText` for updating values)
- **Terminal dependent**: Requires ANSI color support and proper keystroke detection
- **No nested validation**: Property validators are single-level predicates

## Requirements

- Java 21 or higher

## Tech Stack

- **Terminal Abstraction**: [Tetrue Terminal](https://github.com/BFUR64/tetrue-terminal) Lanterna-like abstraction layer for JLine4 and Lanterna
- **Build Tool**: Gradle 9.7.1
- **Language**: Pure Java 21+ (no Kotlin, no reflection, no annotations)
- **Dependencies**: User must provide [Tetrue Terminal](https://github.com/BFUR64/tetrue-terminal) `Terminal`

## Why This Exists

I built Menu Manager while creating a terminal-based Tetris clone, and realized this can be spun-off into its own library. Which I did, and have been promptly updating.

## Contributing

Development takes place on the `dev` branch

See [CONTRIBUTING.md](CONTRIBUTING.md) for contribution guidelines and pull request workflow

