import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Frame;
import java.awt.MenuItem;

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

    // Note: UI actions (clicks, dialogs) can't be tested without full GUI automation tools
}

