import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class KeyControllerTest {

    private KeyController controller;
    private TestPresentation testPresentation;

    // A stub presentation to track method calls without exiting the JVM
    static class TestPresentation extends Presentation {
        boolean next = false;
        boolean prev = false;
        boolean exit = false;

        @Override
        public void nextSlide() { next = true; }

        @Override
        public void prevSlide() { prev = true; }

        @Override
        public void exit(int n) { exit = true; }

        public void reset() {
            next = false;
            prev = false;
            exit = false;
        }
    }

    @BeforeEach
    public void setup() {
        testPresentation = new TestPresentation();
        controller = new KeyController(testPresentation);
    }

    private void simulateKeyPress(int keyCode, char keyChar) {
        KeyEvent event = new KeyEvent(new Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, keyChar);
        controller.keyPressed(event);
    }

    @Test
    public void testKeyQLowerCaseTriggersExit() {
        simulateKeyPress(KeyEvent.VK_Q, 'q');
        assertTrue(testPresentation.exit);
    }

    @Test
    public void testKeyQUpperCaseTriggersExit() {
        testPresentation.reset();
        simulateKeyPress(KeyEvent.VK_Q, 'Q');
        assertTrue(testPresentation.exit);
    }

    @Test
    public void testNextSlidePressedTwice() {
        simulateKeyPress(KeyEvent.VK_PAGE_DOWN, ' ');
        assertTrue(testPresentation.next);
        testPresentation.reset();

        simulateKeyPress(KeyEvent.VK_ENTER, ' ');
        assertTrue(testPresentation.next);
    }

    @Test
    public void testPrevSlidePressedTwice() {
        simulateKeyPress(KeyEvent.VK_PAGE_UP, ' ');
        assertTrue(testPresentation.prev);
        testPresentation.reset();

        simulateKeyPress(KeyEvent.VK_UP, ' ');
        assertTrue(testPresentation.prev);
    }

    @Test
    public void testInvalidKeyDoesNothing() {
        simulateKeyPress(KeyEvent.VK_F1, 'F');
        assertFalse(testPresentation.next);
        assertFalse(testPresentation.prev);
        assertFalse(testPresentation.exit);
    }

    @Test
    public void testSpecialSymbolKeyIgnored() {
        simulateKeyPress(KeyEvent.VK_UNDEFINED, '€');
        assertFalse(testPresentation.exit);
    }

    @Test
    public void testKeyWithModifierStillHandled() {
        KeyEvent event = new KeyEvent(new Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_PAGE_DOWN, ' ');
        controller.keyPressed(event);
        assertTrue(testPresentation.next);
    }

    @Test
    public void testKeyEventWithNullSourceDoesNotThrow() {
        KeyEvent event = new KeyEvent(null, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, ' ');
        assertDoesNotThrow(() -> controller.keyPressed(event));
    }
    @Test
    public void testNextSlideKeys() {
        int[] keys = {
                KeyEvent.VK_PAGE_DOWN,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_ENTER,
                '+'
        };

        for (int key : keys) {
            controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, key, ' '));
            assertTrue(testPresentation.next, "Expected nextSlide() to be called for key: " + key);
            testPresentation.next = false;
        }
    }

    @Test
    public void testPrevSlideKeys() {
        int[] keys = {
                KeyEvent.VK_PAGE_UP,
                KeyEvent.VK_UP,
                '-'
        };

        for (int key : keys) {
            controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, key, ' '));
            assertTrue(testPresentation.prev, "Expected prevSlide() to be called for key: " + key);
            testPresentation.prev = false;
        }
    }

    @Test
    public void testQuitKeysDoNotExitJVMInTest() {
        int[] keys = {'q', 'Q'};

        for (int key : keys) {
            controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, key, (char) key));
            assertTrue(testPresentation.exit, "Expected exit() to be called for key: " + key);
            testPresentation.exit = false;
        }
    }

    @Test
    public void testUnhandledKeyDoesNothing() {
        controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SHIFT, ' '));
        assertFalse(testPresentation.next);
        assertFalse(testPresentation.prev);
        assertFalse(testPresentation.exit);
    }
}
