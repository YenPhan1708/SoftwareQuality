import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

public class KeyControllerTest {

    private KeyController controller;
    private TestPresentation testPresentation;

    // A stub presentation to track method calls without exiting the JVM
    static class TestPresentation extends Presentation {
        boolean nextCalled = false;
        boolean prevCalled = false;
        boolean exitCalled = false;

        @Override
        public void nextSlide() {
            nextCalled = true;
        }

        @Override
        public void prevSlide() {
            prevCalled = true;
        }

        @Override
        public void exit(int n) {
            exitCalled = true;
        }
    }

    @BeforeEach
    public void setup() {
        testPresentation = new TestPresentation();
        controller = new KeyController(testPresentation);
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
            assertTrue(testPresentation.nextCalled, "Expected nextSlide() to be called for key: " + key);
            testPresentation.nextCalled = false;
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
            assertTrue(testPresentation.prevCalled, "Expected prevSlide() to be called for key: " + key);
            testPresentation.prevCalled = false;
        }
    }

    @Test
    public void testQuitKeysDoNotExitJVMInTest() {
        int[] keys = {'q', 'Q'};

        for (int key : keys) {
            controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, key, (char) key));
            assertTrue(testPresentation.exitCalled, "Expected exit() to be called for key: " + key);
            testPresentation.exitCalled = false;
        }
    }

    @Test
    public void testUnhandledKeyDoesNothing() {
        controller.keyPressed(new KeyEvent(new java.awt.Label(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_SHIFT, ' '));
        assertFalse(testPresentation.nextCalled);
        assertFalse(testPresentation.prevCalled);
        assertFalse(testPresentation.exitCalled);
    }
}
