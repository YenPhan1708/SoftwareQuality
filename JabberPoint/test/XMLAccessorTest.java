import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class XMLAccessorTest
{
    private XMLAccessor accessor;
    private Presentation presentation;

    @BeforeEach
    public void setup()
    {
        accessor = new XMLAccessor();
        presentation = new Presentation();
    }

    @Test
    public void testSaveFileCreatesXml()
    {
        presentation.setTitle("Saved Presentation");
        presentation.append(new Slide());

        assertDoesNotThrow(() -> accessor.saveFile(presentation, "TestFile/test-output.xml"));
    }

    @Test
    public void testLoadInvalidFileThrowsException()
    {
        assertThrows(IOException.class, () -> accessor.loadFile(presentation, "nonexistent.xml"));
    }

    @Test
    public void testSaveAndLoadRoundTrip() throws IOException
    {
        presentation.setTitle("RoundTrip Title");
        Slide slide = new Slide();
        slide.setTitle("RoundTrip Slide");
        slide.append(1, "Item A");
        presentation.append(slide);

        String path = "TestFile/roundtrip.xml";
        accessor.saveFile(presentation, path);

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, path);

        assertEquals("RoundTrip Title", loaded.getTitle());
        assertEquals(1, loaded.getSize());
        assertEquals("RoundTrip Slide", loaded.getSlide(0).getTitle());
    }

    @Test
    public void testSaveEmptyPresentationDoesNotThrow()
    {
        Presentation emptyPres = new Presentation();
        assertDoesNotThrow(() -> accessor.saveFile(emptyPres, "TestFile/empty.xml"));
    }

    @Test
    public void testSaveFileWithNullFilenameThrows()
    {
        Presentation pres = new Presentation();
        assertThrows(NullPointerException.class, () -> accessor.saveFile(pres, null));
    }

    @Test
    public void testLoadFileWithNullPresentationThrows()
    {
        assertThrows(NullPointerException.class, () -> accessor.loadFile(null, "file.xml"));
    }

    @Test
    public void testSaveEmptyPresentation()
    {
        Presentation p = new Presentation();
        assertDoesNotThrow(() -> accessor.saveFile(p, "TestFile/empty-presentation.xml"));
    }

    @Test
    public void testAppendNullSlideShouldThrowException()
    {
        Presentation p = new Presentation();
        assertThrows(IllegalArgumentException.class, () -> p.append(null));
    }

    @Test
    public void testSavePresentationWithNoTitle()
    {
        Presentation p = new Presentation(); // no title
        p.append(new Slide());
        assertDoesNotThrow(() -> accessor.saveFile(p, "TestFile/no-title.xml"));
    }

    @Test
    public void testSaveFileWithSpecialCharactersInPath()
    {
        Presentation p = new Presentation();
        p.setTitle("Special");
        assertDoesNotThrow(() -> accessor.saveFile(p, "TestFile/test_čćžđš.xml"));
    }

    @Test
    public void testSaveAndLoadPresentationWithUnicode() throws IOException
    {
        Presentation p = new Presentation();
        p.setTitle("標題 ");
        Slide slide = new Slide();
        slide.setTitle("幻燈片");
        slide.append(1, "這是內容 with emoji :)");
        p.append(slide);

        String file = "TestFile/unicode-test.xml";
        accessor.saveFile(p, file);

        Presentation loaded = new Presentation();
        accessor.loadFile(loaded, file);

        assertEquals("標題 ", loaded.getTitle());
        assertEquals("幻燈片", loaded.getSlide(0).getTitle());
    }

    @Test
    public void testRepeatedSaveAndLoadDoesNotCorrupt() throws IOException
    {
        Presentation p = new Presentation();
        p.setTitle("Repeat Test");
        p.append(new Slide());

        for (int i = 0; i < 5; i++) {
            accessor.saveFile(p, "TestFile/repeat-test.xml");

            Presentation loaded = new Presentation();
            accessor.loadFile(loaded, "TestFile/repeat-test.xml");

            assertEquals("Repeat Test", loaded.getTitle());
        }
    }

    @Test
    public void testSaveWithVeryLongFilename()
    {
        Presentation p = new Presentation();
        StringBuilder name = new StringBuilder("long_filename_");
        for (int i = 0; i < 200; i++) name.append("x");
        name.append(".xml");

        assertDoesNotThrow(() -> accessor.saveFile(p, name.toString()));
    }
}

