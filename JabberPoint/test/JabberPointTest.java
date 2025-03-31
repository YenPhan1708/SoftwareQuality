import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class JabberPointTest {

    private JabberPoint jabberPoint;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
        jabberPoint = new JabberPoint(presentation);
    }

    @Test
    public void testConstructorInitializesPresentation() {
        assertNotNull(jabberPoint.getPresentation());
        assertEquals(presentation, jabberPoint.getPresentation());
    }

    @Test
    public void testCreateAccessorReturnsDemoPresentation() {
        Accessor accessor = jabberPoint.createAccessor();
        assertNotNull(accessor);
        assertTrue(accessor instanceof DemoPresentation);
    }

    @Test
    public void testMainLoadsDemoWhenNoArgs() {
        assertDoesNotThrow(() -> {
            Method mainMethod = JabberPoint.class.getMethod("main", String[].class);
            String[] args = new String[0];

            // Call main with no args (should load demo)
            mainMethod.invoke(null, (Object) args);
        });
    }

    @Test
    public void testMainLoadsWithFileArg() {
        assertDoesNotThrow(() -> {
            Method mainMethod = JabberPoint.class.getMethod("main", String[].class);
            String[] args = new String[]{"test.xml"};

            // Even if file not found, catch handled internally (IOException)
            mainMethod.invoke(null, (Object) args);
        });
    }
}
