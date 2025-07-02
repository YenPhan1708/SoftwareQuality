import Slide.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Presentation.*;
import Controller.*;

import java.awt.MenuBar;
import java.awt.event.KeyListener;

import static org.junit.jupiter.api.Assertions.*;

public class SlideViewerFrameTest {

    private Presentation presentation;
    private SlideViewerFrame frame;

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
        frame = new SlideViewerFrame("Test Title", presentation);
    }

    @Test
    public void testConstructorInitializesFrame() {
        assertEquals("Jabberpoint 1.6 - OU", frame.getTitle(), "Title should be overridden");
        assertEquals(SlideViewerFrame.WIDTH, frame.getWidth());
        assertEquals(SlideViewerFrame.HEIGHT, frame.getHeight());
        assertTrue(frame.isVisible());
    }

    @Test
    public void testFrameHasKeyController() {
        KeyListener[] listeners = frame.getKeyListeners();
        boolean found = false;
        for (KeyListener l : listeners) {
            if (l instanceof KeyController) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Frame should have a KeyController");
    }

    @Test
    public void testMenuBarIsSet() {
        MenuBar menuBar = frame.getMenuBar();
        assertNotNull(menuBar);
    }

    @Test
    public void testSlideViewerComponentIsPresent() {
        assertEquals(1, frame.getContentPane().getComponentCount());
        assertTrue(frame.getContentPane().getComponent(0) instanceof SlideViewerComponent);
    }

    @Test
    public void testMultipleFramesAreIndependent() {
        SlideViewerFrame f1 = new SlideViewerFrame("Frame 1", new Presentation());
        SlideViewerFrame f2 = new SlideViewerFrame("Frame 2", new Presentation());

        assertNotSame(f1, f2);
        assertNotSame(f1.getContentPane(), f2.getContentPane());
    }

    @Test
    public void testFrameSizeMatchesStaticConstants() {
        SlideViewerFrame f = new SlideViewerFrame("Size Test", new Presentation());
        assertEquals(SlideViewerFrame.WIDTH, f.getWidth());
        assertEquals(SlideViewerFrame.HEIGHT, f.getHeight());
    }

    @Test
    public void testMenuBarIsSameInstanceOnMultipleCalls() {
        SlideViewerFrame f = new SlideViewerFrame("Menu Test", new Presentation());
        MenuBar bar1 = f.getMenuBar();
        MenuBar bar2 = f.getMenuBar();
        assertSame(bar1, bar2);
    }

    @Test
    public void testOnlyOneKeyControllerRegistered() {
        SlideViewerFrame f = new SlideViewerFrame("Key Test", new Presentation());
        long count = java.util.Arrays.stream(f.getKeyListeners())
                .filter(k -> k instanceof KeyController)
                .count();
        assertEquals(1, count);
    }

    @Test
    public void testFrameIsVisibleByDefault() {
        SlideViewerFrame f = new SlideViewerFrame("Visibility", new Presentation());
        assertTrue(f.isVisible());
    }

    @Test
    public void testDisposeDoesNotThrow() {
        SlideViewerFrame f = new SlideViewerFrame("Dispose Test", new Presentation());
        assertDoesNotThrow(f::dispose);
    }
}
