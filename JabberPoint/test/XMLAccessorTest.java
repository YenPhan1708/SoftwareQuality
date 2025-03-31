import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class XMLAccessorTest {

    private XMLAccessor accessor;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        accessor = new XMLAccessor();
        presentation = new Presentation();
    }

    @Test
    public void testSaveFileCreatesXml() {
        presentation.setTitle("Saved Presentation");
        presentation.append(new Slide());

        assertDoesNotThrow(() -> accessor.saveFile(presentation, "test-output.xml"));
    }

    @Test
    public void testLoadInvalidFileThrowsException() {
        assertThrows(IOException.class, () -> accessor.loadFile(presentation, "nonexistent.xml"));
    }

    @Test
    public void testSaveAndLoadRoundTrip() throws IOException {
        presentation.setTitle("RoundTrip Title");
        Slide slide = new Slide();
        slide.setTitle("RoundTrip Slide");
        slide.append(1, "Item A");
        presentation.append(slide);

        String path = "roundtrip.xml";
        accessor.saveFile(presentation, path);

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, path);

        assertEquals("RoundTrip Title", loaded.getTitle());
        assertEquals(1, loaded.getSize());
        assertEquals("RoundTrip Slide", loaded.getSlide(0).getTitle());
    }
}

