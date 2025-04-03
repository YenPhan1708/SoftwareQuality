import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DemoPresentationText
{

    private DemoPresentation demoPresentation;
    private Presentation presentation;

    @BeforeEach
    public void setup()
    {
        demoPresentation = new DemoPresentation();
        presentation = new Presentation();
    }

    @Test
    public void testLoadFileAddsSlides() throws IOException
    {
        demoPresentation.loadFile(presentation, "ignored.xml");

        assertEquals("Demo Presentation", presentation.getTitle());
        assertEquals(3, presentation.getSize());

        assertNotNull(presentation.getSlide(0));
        assertNotNull(presentation.getSlide(1));
        assertNotNull(presentation.getSlide(2));
    }

    @Test
    public void testSaveFileThrowsException()
    {
        assertThrows(IllegalStateException.class, () -> {
            demoPresentation.saveFile(presentation, "anything.xml");
        });
    }

    @Test
    public void testLoadFileWithNullFilename()
    {
        assertDoesNotThrow(() -> demoPresentation.loadFile(presentation, null));
        assertEquals(3, presentation.getSize());  // Still loads demo
    }

    @Test
    public void testLoadFileWithNullPresentation()
    {
        assertThrows(NullPointerException.class, () -> demoPresentation.loadFile(null, "anything.xml"));
    }

    @Test
    public void testLoadFileTwiceResetsSlides() throws IOException
    {
        demoPresentation.loadFile(presentation, "");
        int firstLoadCount = presentation.getSize();

        demoPresentation.loadFile(presentation, "");
        int secondLoadCount = presentation.getSize();

        assertEquals(firstLoadCount, secondLoadCount);
        assertEquals("Demo Presentation", presentation.getTitle());
    }

}
