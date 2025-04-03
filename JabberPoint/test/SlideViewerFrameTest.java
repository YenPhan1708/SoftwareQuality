import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.MenuBar;
import java.awt.event.KeyListener;

import static org.junit.jupiter.api.Assertions.*;

public class SlideViewerFrameTest
{

    private Presentation presentation;
    private SlideViewerFrame frame;

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
        frame = new SlideViewerFrame("Test Title", presentation);
    }

    @Test
    public void testConstructorInitializesFrame()
    {
        assertEquals("Jabberpoint 1.6 - OU", frame.getTitle()); // overridden title
        assertEquals(SlideViewerFrame.WIDTH, frame.getWidth());
        assertEquals(SlideViewerFrame.HEIGHT, frame.getHeight());
        assertTrue(frame.isVisible());
    }

    @Test
    public void testFrameHasKeyListener()
    {
        KeyListener[] listeners = frame.getKeyListeners();
        boolean hasKeyController = false;
        for (KeyListener l : listeners)
        {
            if (l instanceof KeyController)
            {
                hasKeyController = true;
                break;
            }
        }
        assertTrue(hasKeyController, "Frame should register KeyController");
    }

    @Test
    public void testMenuBarIsSet()
    {
        MenuBar menuBar = frame.getMenuBar();
        assertNotNull(menuBar);
    }

    @Test
    public void testSlideViewerComponentIsSet()
    {
        assertEquals(1, frame.getContentPane().getComponentCount());
        assertTrue(frame.getContentPane().getComponent(0) instanceof SlideViewerComponent);
    }

    @Test
    public void testMultipleFramesCreatedIndependently()
    {
        SlideViewerFrame frame1 = new SlideViewerFrame("Frame 1", new Presentation());
        SlideViewerFrame frame2 = new SlideViewerFrame("Frame 2", new Presentation());

        assertNotEquals(frame1, frame2);
        assertNotSame(frame1.getContentPane(), frame2.getContentPane());
    }

    @Test
    public void testFrameSizeRespectsStaticWidthHeight()
    {
        SlideViewerFrame frame = new SlideViewerFrame("Size Test", new Presentation());
        assertEquals(SlideViewerFrame.WIDTH, frame.getWidth());
        assertEquals(SlideViewerFrame.HEIGHT, frame.getHeight());
    }

    @Test
    public void testMenuBarIsSetAndSameInstance()
    {
        SlideViewerFrame frame = new SlideViewerFrame("Menu Test", new Presentation());
        MenuBar bar1 = frame.getMenuBar();
        MenuBar bar2 = frame.getMenuBar();
        assertSame(bar1, bar2);
    }

    @Test
    public void testKeyControllerExistsOnlyOnce()
    {
        SlideViewerFrame frame = new SlideViewerFrame("Key Test", new Presentation());
        KeyListener[] listeners = frame.getKeyListeners();
        long keyControllers = java.util.Arrays.stream(listeners)
                .filter(k -> k instanceof KeyController).count();
        assertEquals(1, keyControllers);
    }

    @Test
    public void testFrameIsVisibleByDefault()
    {
        SlideViewerFrame frame = new SlideViewerFrame("Visibility", new Presentation());
        assertTrue(frame.isVisible());
    }

    @Test
    public void testFrameDisposesWithoutException()
    {
        SlideViewerFrame frame = new SlideViewerFrame("Dispose Test", new Presentation());
        assertDoesNotThrow(frame::dispose);
    }
}

