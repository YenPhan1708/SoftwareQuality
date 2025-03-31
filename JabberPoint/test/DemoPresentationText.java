import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class DemoPresentationText {

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

        assertEquals("Demo Presentation", presentation.getTitle());
        assertEquals(3, presentation.getSize());

        assertNotNull(presentation.getSlide(0));
        assertNotNull(presentation.getSlide(1));
        assertNotNull(presentation.getSlide(2));
    }

    @Test
    public void testSaveFileThrowsException() {
        assertThrows(IllegalStateException.class, () -> {
            demoPresentation.saveFile(presentation, "anything.xml");
        });
    }
}
