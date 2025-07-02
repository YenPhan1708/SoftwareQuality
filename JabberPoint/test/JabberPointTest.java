import org.junit.jupiter.api.Test;
import Presentation.Presentation;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

public class JabberPointTest {

    @Test
    public void testMainWithNoArgsLoadsDemoPresentation() {
        assertDoesNotThrow(() -> {
            Method mainMethod = JabberPoint.class.getMethod("main", String[].class);
            String[] args = new String[0];
            mainMethod.invoke(null, (Object) args);
        });
    }

    @Test
    public void testMainWithOneFakeFile() {
        assertDoesNotThrow(() -> {
            Method mainMethod = JabberPoint.class.getMethod("main", String[].class);
            String[] args = new String[]{"nonexistent.xml"};
            mainMethod.invoke(null, (Object) args);
        });
    }

    @Test
    public void testMainWithEmptyStringArg() {
        assertDoesNotThrow(() -> {
            Method main = JabberPoint.class.getMethod("main", String[].class);
            String[] args = {""};
            main.invoke(null, (Object) args);
        });
    }

    @Test
    public void testMainWithMultipleArgs() {
        assertDoesNotThrow(() -> {
            Method main = JabberPoint.class.getMethod("main", String[].class);
            String[] args = {"test.xml", "extra.xml"};
            main.invoke(null, (Object) args);
        });
    }

    @Test
    public void testMainWithNullArgsArray() {
        assertDoesNotThrow(() -> {
            Method main = JabberPoint.class.getMethod("main", String[].class);
            main.invoke(null, (Object) null);
        });
    }
}
