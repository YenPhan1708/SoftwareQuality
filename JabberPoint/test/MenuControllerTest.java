import Controller.MenuController;
import Presentation.Presentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;

public class MenuControllerTest {

    private MenuController menuController;
    private Frame dummyFrame;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        dummyFrame = new Frame();  // Headless test-safe frame
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
    public void testMenuControllerConstructorInitializes() {
        assertNotNull(menuController); // Sanity check
    }

    @Test
    public void testMkMenuItemWithNullLabelThrows() {
        assertThrows(NullPointerException.class, () ->
                menuController.mkMenuItem(null));
    }

    @Test
    public void testMkMenuItemWithEmptyStringThrows() {
        assertThrows(StringIndexOutOfBoundsException.class, () ->
                menuController.mkMenuItem(""));
    }

    @Test
    public void testMkMenuItemCreatesDistinctInstances() {
        MenuItem item1 = menuController.mkMenuItem("Item A");
        MenuItem item2 = menuController.mkMenuItem("Item A");

        assertNotSame(item1, item2);
        assertEquals("Item A", item1.getLabel());
        assertEquals("Item A", item2.getLabel());
    }

    @Test
    public void testMenuItemHasActionListenerAttached() {
        // Get "File" menu and its second item: "New"
        Menu fileMenu = menuController.getMenu(0);
        MenuItem newItem = fileMenu.getItem(1);

        ActionListener[] listeners = newItem.getActionListeners();
        assertNotNull(listeners);
        assertTrue(listeners.length > 0, "Expected at least one ActionListener");
    }

    @Test
    public void testCreatedMenuItemHasShortcut() {
        MenuItem item = menuController.mkMenuItem("Something");
        assertNotNull(item.getShortcut());
    }

    @Test
    public void testFileMenuHasExpectedItems() {
        Menu fileMenu = menuController.getMenu(0);
        assertNotNull(fileMenu);
        assertTrue(fileMenu.getItemCount() >= 2, "File menu should have multiple items");
    }

    @Test
    public void testEachMenuItemInFileMenuIsValid() {
        Menu fileMenu = menuController.getMenu(0);
        for (int i = 0; i < fileMenu.getItemCount(); i++) {
            MenuItem item = fileMenu.getItem(i);
            assertNotNull(item.getLabel());
            assertTrue(item.getLabel().length() > 0);
        }
    }
}
