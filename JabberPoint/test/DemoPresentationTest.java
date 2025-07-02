import Accessor.DemoPresentation;
import Presentation.Presentation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DemoPresentationTest {

    private DemoPresentation demoPresentation;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        demoPresentation = new DemoPresentation();
        presentation = new Presentation();
    }

    @Test
    public void testLoadFileAddsSlides() throws IOException {
        demoPresentation.loadFile(presentation, "ignored.xml");

        assertEquals(3, presentation.getSize()); // Demo contains 3 slides
        assertNotNull(presentation.getSlide(0));
        assertNotNull(presentation.getSlide(1));
        assertNotNull(presentation.getSlide(2));

        // Optional: check if toString contains the word "Demo"
        String desc = presentation.toString().toLowerCase();
        assertTrue(desc.contains("demo") || desc.contains("jabberpoint"));
    }

    @Test
    public void testSaveFileThrowsException() {
        assertThrows(IllegalStateException.class, () ->
                demoPresentation.saveFile(presentation, "ignored.xml"));
    }

    @Test
    public void testLoadFileWithNullFilenameStillLoadsDemo() {
        assertDoesNotThrow(() -> demoPresentation.loadFile(presentation, null));
        assertEquals(3, presentation.getSize());
    }

    @Test
    public void testLoadFileWithNullPresentationThrows() {
        assertThrows(NullPointerException.class, () ->
                demoPresentation.loadFile(null, "ignored.xml"));
    }

    @Test
    public void testLoadFileTwiceReplacesSlides() throws IOException {
        demoPresentation.loadFile(presentation, "");
        int firstLoad = presentation.getSize();

        demoPresentation.loadFile(presentation, "");
        int secondLoad = presentation.getSize();

        assertEquals(firstLoad, secondLoad);
    }
}
