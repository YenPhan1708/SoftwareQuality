import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class AccessorTest
{
    private Accessor accessor;
    private Presentation presentation;

    @BeforeEach
    public void setup()
    {
        accessor = new DemoPresentation();
        presentation = new Presentation();
    }

    @Test
    public void testLoadFileLoadsDemoSlides() throws IOException
    {
        accessor.loadFile(presentation, "");
        assertEquals("Demo Presentation", presentation.getTitle());
        assertEquals(3, presentation.getSize(), "Should contain 3 demo slides");
    }


    @Test
    public void testSaveFileThrowsException()
    {
        assertThrows(IllegalStateException.class, () -> {
            accessor.saveFile(presentation, "whatever.xml");
        });
    }
}

