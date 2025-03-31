import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Frame;
import java.awt.MenuItem;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class MenuControllerTest {

    private MenuController menuController;
    private Frame dummyFrame;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        dummyFrame = new Frame();  // GUI shell for testing
        presentation = new Presentation();
        menuController = new MenuController(dummyFrame, presentation);
    }

    @Test
    public void testMkMenuItemCreatesMenuItem() {
        MenuItem item = menuController.mkMenuItem("TestItem");
        assertNotNull(item);
        assertEquals("TestItem", item.getLabel());
        assertNotNull(item.getShortcut());
    }

    @Test
    public void testMenuControllerConstructor() {
        assertNotNull(menuController); // Just ensures it initializes without crashing
    }

    @Test
    public void testMkMenuItemWithNullLabelDoesNotThrow() {
        assertDoesNotThrow(() -> {
            MenuItem item = menuController.mkMenuItem(null);
            assertNotNull(item);
        });
    }

    @Test
    public void testMkMenuItemWithEmptyString() {
        MenuItem item = menuController.mkMenuItem("");
        assertNotNull(item);
        assertEquals("", item.getLabel());
    }

    @Test
    public void testMkMenuItemCreatesUniqueInstances() {
        MenuItem item1 = menuController.mkMenuItem("Item A");
        MenuItem item2 = menuController.mkMenuItem("Item A");

        assertNotSame(item1, item2);
        assertEquals("Item A", item1.getLabel());
        assertEquals("Item A", item2.getLabel());
    }

    @Test
    public void testMenuItemHasActionListenerAttached() {
        MenuItem item = menuController.mkMenuItem("Trigger");
        ActionListener[] listeners = item.getActionListeners();

        assertNotNull(listeners);
        assertTrue(listeners.length > 0, "Expected action listener attached to menu item");
    }

    @Test
    public void testMenuItemShortcutExists() {
        MenuItem item = menuController.mkMenuItem("Something");
        assertNotNull(item.getShortcut(), "Expected shortcut to be set");
    }
}

