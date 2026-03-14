package io.github.bfur64.menu.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KeyReaderTest {

    private KeyReader keyReader;

    @BeforeEach
    public void setUp() {
        keyReader = new KeyReader();
    }

    @Test
    public void testIsKeyPressed() {
        // Simulate key press
        keyReader.pressKey('A');

        // Verify that the key is pressed
        assertTrue(keyReader.isKeyPressed('A'));
    }

    @Test
    public void testIsKeyPressedNotPressed() {
        // Verify that a key not pressed returns false
        assertFalse(keyReader.isKeyPressed('B'));
    }

    @Test
    public void testIsKeyPressedAfterRelease() {
        // Simulate key press and release
        keyReader.pressKey('C');
        keyReader.releaseKey('C');

        // Verify that the key is not pressed after release
        assertFalse(keyReader.isKeyPressed('C'));
    }
}