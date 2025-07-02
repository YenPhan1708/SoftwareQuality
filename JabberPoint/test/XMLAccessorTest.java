import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Accessor.XMLAccessor;
import Presentation.Presentation;
import Slide.Slide;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class XMLAccessorTest
{
    private XMLAccessor accessor;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        accessor = new XMLAccessor();
        presentation = new Presentation();
    }

    @Test
    public void testSaveFileCreatesXml() {
        presentation.setShowTitle("Saved Presentation");
        Slide slide = new Slide();
        slide.setTitle("Test Slide");
        presentation.addSlide(slide);

        assertDoesNotThrow(() -> accessor.saveFile(presentation, "TestFile/test-output.xml"));
        assertTrue(new File("TestFile/test-output.xml").exists());
    }

    @Test
    public void testLoadInvalidFileThrowsException() {
        assertThrows(IOException.class, () -> accessor.loadFile(presentation, "nonexistent.xml"));
    }

    @Test
    public void testSaveAndLoadRoundTrip() throws IOException {
        presentation.setShowTitle("RoundTrip Title");
        Slide slide = new Slide();
        slide.setTitle("RoundTrip Slide");
        slide.appendTextItem(1, "Item A");
        presentation.addSlide(slide);

        String path = "TestFile/roundtrip.xml";
        accessor.saveFile(presentation, path);

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, path);

        assertEquals("RoundTrip Title", loaded.getShowTitle());
        assertEquals(1, loaded.getSize());
        assertEquals("RoundTrip Slide", loaded.getSlide(0).getTitle());
    }

    @Test
    public void testSaveEmptyPresentationDoesNotThrow() {
        assertDoesNotThrow(() -> accessor.saveFile(presentation, "TestFile/empty.xml"));
    }

    @Test
    public void testSaveFileWithNullFilenameThrows() {
        assertThrows(NullPointerException.class, () -> accessor.saveFile(presentation, null));
    }

    @Test
    public void testLoadFileWithNullPresentationThrows() {
        assertThrows(NullPointerException.class, () -> accessor.loadFile(null, "file.xml"));
    }

    @Test
    public void testAppendNullSlideShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> presentation.addSlide(null));
    }

    @Test
    public void testSavePresentationWithNoTitle() {
        Slide slide = new Slide();
        slide.setTitle(null);  // title is optional
        presentation.addSlide(slide);

        assertDoesNotThrow(() -> accessor.saveFile(presentation, "TestFile/no-title.xml"));
    }

    @Test
    public void testSaveFileWithSpecialCharactersInPath() {
        presentation.setShowTitle("Special");
        assertDoesNotThrow(() -> accessor.saveFile(presentation, "TestFile/test_čćžđš.xml"));
    }

    @Test
    public void testSaveAndLoadPresentationWithUnicode() throws IOException {
        presentation.setShowTitle("標題 ");
        Slide slide = new Slide();
        slide.setTitle("幻燈片");
        slide.appendTextItem(1, "這是內容 with emoji 😊");
        presentation.addSlide(slide);

        String path = "TestFile/unicode-test.xml";
        accessor.saveFile(presentation, path);

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, path);

        assertEquals("標題 ", loaded.getShowTitle());
        assertEquals("幻燈片", loaded.getSlide(0).getTitle());
    }

    @Test
    public void testRepeatedSaveAndLoadDoesNotCorrupt() throws IOException {
        presentation.setShowTitle("Repeat Test");
        Slide slide = new Slide();
        slide.setTitle("Repeated Slide");
        presentation.addSlide(slide);

        String path = "TestFile/repeat-test.xml";

        for (int i = 0; i < 5; i++) {
            accessor.saveFile(presentation, path);

            Presentation loaded = new Presentation();
            accessor.loadFile(loaded, path);

            assertEquals("Repeat Test", loaded.getShowTitle());
            assertEquals("Repeated Slide", loaded.getSlide(0).getTitle());
        }
    }

    @Test
    public void testSaveWithVeryLongFilename() {
        StringBuilder name = new StringBuilder("TestFile/long_filename_");
        for (int i = 0; i < 200; i++) name.append("x");
        name.append(".xml");

        assertDoesNotThrow(() -> accessor.saveFile(presentation, name.toString()));
    }
}
