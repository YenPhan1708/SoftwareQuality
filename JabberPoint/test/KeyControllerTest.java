import Controller.KeyController;
import Presentation.Presentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class KeyControllerTest {

    private KeyController controller;
    private TestPresentation testPresentation;

    // A stub Presentation class to track method calls
    static class TestPresentation extends Presentation {
        boolean next = false;
        boolean prev = false;
        boolean exit = false;

        @Override
        public void nextSlide() {
            next = true;
        }

        @Override
        public void prevSlide() {
            prev = true;
        }

        @Override
        public void exit(int n) {
            exit = true;
        }

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
    public void testLowerCaseQTriggersExit() {
        simulateKeyPress(KeyEvent.VK_Q, 'q');
        assertTrue(testPresentation.exit);
    }

    @Test
    public void testUpperCaseQTriggersExit() {
        testPresentation.reset();
        simulateKeyPress(KeyEvent.VK_Q, 'Q');
        assertTrue(testPresentation.exit);
    }

    @Test
    public void testNextSlideKeysTriggerNext() {
        int[] keys = {
                KeyEvent.VK_PAGE_DOWN,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_ENTER,
                '+'
        };
        for (int key : keys) {
            simulateKeyPress(key, ' ');
            assertTrue(testPresentation.next, "Expected nextSlide() for key: " + key);
            testPresentation.reset();
        }
    }

    @Test
    public void testPrevSlideKeysTriggerPrev() {
        int[] keys = {
                KeyEvent.VK_PAGE_UP,
                KeyEvent.VK_UP,
                '-'
        };
        for (int key : keys) {
            simulateKeyPress(key, ' ');
            assertTrue(testPresentation.prev, "Expected prevSlide() for key: " + key);
            testPresentation.reset();
        }
    }

    @Test
    public void testUnhandledKeyDoesNothing() {
        simulateKeyPress(KeyEvent.VK_F1, 'F');
        assertFalse(testPresentation.next);
        assertFalse(testPresentation.prev);
        assertFalse(testPresentation.exit);
    }

    @Test
    public void testUndefinedKeyDoesNothing() {
        simulateKeyPress(KeyEvent.VK_UNDEFINED, '€');
        assertFalse(testPresentation.exit);
    }

    @Test
    public void testKeyWithModifierStillTriggersNext() {
        KeyEvent event = new KeyEvent(new Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), KeyEvent.SHIFT_DOWN_MASK, KeyEvent.VK_PAGE_DOWN, ' ');
        controller.keyPressed(event);
        assertTrue(testPresentation.next);
    }

    @Test
    public void testNullKeyEventSourceThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                new KeyEvent(null, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, ' ')
        );
    }

    @Test
    public void testQuitKeysDoNotExitJVMInTest() {
        int[] keys = {'q', 'Q'};
        for (int key : keys) {
            controller.keyPressed(new KeyEvent(new Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, key, (char) key));
            assertTrue(testPresentation.exit, "Expected exit() for key: " + (char) key);
            testPresentation.exit = false;
        }
    }
}
