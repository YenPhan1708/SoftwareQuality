import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertEquals("Jabberpoint 1.6 - OU", frame.getTitle()); // overridden title
        assertEquals(SlideViewerFrame.WIDTH, frame.getWidth());
        assertEquals(SlideViewerFrame.HEIGHT, frame.getHeight());
        assertTrue(frame.isVisible());
    }

    @Test
    public void testFrameHasKeyListener() {
        KeyListener[] listeners = frame.getKeyListeners();
        boolean hasKeyController = false;
        for (KeyListener l : listeners) {
            if (l instanceof KeyController) {
                hasKeyController = true;
                break;
            }
        }
        assertTrue(hasKeyController, "Frame should register KeyController");
    }

    @Test
    public void testMenuBarIsSet() {
        MenuBar menuBar = frame.getMenuBar();
        assertNotNull(menuBar);
    }

    @Test
    public void testSlideViewerComponentIsSet() {
        assertEquals(1, frame.getContentPane().getComponentCount());
        assertTrue(frame.getContentPane().getComponent(0) instanceof SlideViewerComponent);
    }
}

