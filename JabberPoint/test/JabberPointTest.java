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

    @Test
    public void testCreateAccessorAlwaysReturnsDemoPresentation() {
        JabberPoint app = new JabberPoint(new Presentation());
        for (int i = 0; i < 5; i++) {
            assertTrue(app.createAccessor() instanceof DemoPresentation);
        }
    }

    @Test
    public void testMainWithNullArgsDoesNotCrash() throws Exception {
        Method main = JabberPoint.class.getMethod("main", String[].class);
        assertDoesNotThrow(() -> main.invoke(null, (Object) null));
    }

    @Test
    public void testMainWithEmptyArgString() throws Exception {
        Method main = JabberPoint.class.getMethod("main", String[].class);
        String[] args = {""};
        assertDoesNotThrow(() -> main.invoke(null, (Object) args));
    }

    @Test
    public void testMainWithMultipleArgs() throws Exception {
        Method main = JabberPoint.class.getMethod("main", String[].class);
        String[] args = {"file1.xml", "extra.xml"};
        assertDoesNotThrow(() -> main.invoke(null, (Object) args));
    }

    @Test
    public void testMainWithFakeFileTriggersIOExceptionHandling() throws Exception {
        Method main = JabberPoint.class.getMethod("main", String[].class);
        String[] args = {"nonexistent-file.xml"};
        assertDoesNotThrow(() -> main.invoke(null, (Object) args));  // should catch and print stack trace internally
    }

    @Test
    public void testConstructorWithNullPresentationDoesNotThrow() {
        assertDoesNotThrow(() -> new JabberPoint(null));
    }
}
