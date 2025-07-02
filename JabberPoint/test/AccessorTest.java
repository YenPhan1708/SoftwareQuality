import Accessor.Accessor;
import Accessor.XMLAccessor;
import Presentation.Presentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AccessorTest {

    private Accessor accessor;
    private Presentation presentation;

    @BeforeEach
    public void setup()
    {
        accessor = new XMLAccessor(); // Or use your actual XMLAccessor
        presentation = new Presentation();
    }

    @Test
    public void testLoadFileWithNonexistentFileThrowsIOException()
    {
        assertThrows(IOException.class, () -> {
            accessor.loadFile(presentation, "nonexistent.xml");
        });
    }

    @Test
    public void testSaveFileThrowsUnsupportedException()
    {
        assertThrows(IllegalStateException.class, () -> {
            accessor.saveFile(presentation, "output.xml");
        });
    }
}
