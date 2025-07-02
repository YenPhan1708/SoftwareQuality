import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Presentation.*;
import Accessor.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PresentationManagerTest
{

    private TestPresentationManager manager;
    private Presentation presentation;

    static class DummyAccessor implements Accessor {
        boolean loaded = false;
        boolean saved = false;

        @Override
        public void loadFile(Presentation p, String filename) throws IOException {
            if (p == null || filename == null || filename.isEmpty()) {
                throw new NullPointerException("Filename must not be null or empty");
            }
            loaded = true;
        }

        @Override
        public void saveFile(Presentation p, String filename) throws IOException {
            if (p == null || filename == null || filename.isEmpty()) {
                throw new NullPointerException("Filename must not be null or empty");
            }
            saved = true;
        }
    }

    static class TestPresentationManager extends PresentationManager
    {
        DummyAccessor dummyAccessor;

        public TestPresentationManager()
        {
            this.dummyAccessor = new DummyAccessor();
        }

        @Override
        public Accessor createAccessor()
        {
            return dummyAccessor;
        }

        public DummyAccessor getDummyAccessor()
        {
            return dummyAccessor;
        }
    }

    @BeforeEach
    public void setup()
    {
        presentation = new Presentation();
        manager = new TestPresentationManager();
    }

    @Test
    public void testCreateAccessorReturnsSameInstance() {
        Accessor first = manager.createAccessor();
        Accessor second = manager.createAccessor();
        assertSame(first, second, "Accessor should be cached and reused");
    }

    @Test
    public void testLoadFileCallsAccessor() throws IOException
    {
        manager.createAccessor().loadFile(presentation, "test.xml");
        assertTrue(manager.getDummyAccessor().loaded);
    }

    @Test
    public void testSaveFileCallsAccessor() throws IOException
    {
        manager.createAccessor().saveFile(presentation, "test.xml");
        assertTrue(manager.getDummyAccessor().saved);
    }

    @Test
    public void testLoadFileWithNullFilenameThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().loadFile(presentation, null));
    }

    @Test
    public void testSaveFileWithNullFilenameThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().saveFile(presentation, null));
    }

    @Test
    public void testLoadFileWithNullPresentationThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().loadFile(null, "test.xml"));
    }

    @Test
    public void testSaveFileWithNullPresentationThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().saveFile(null, "test.xml"));
    }

    @Test
    public void testSaveFileWithEmptyFilenameThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().saveFile(presentation, ""));
    }

    @Test
    public void testLoadFileWithEmptyFilenameThrows()
    {
        assertThrows(NullPointerException.class, () -> manager.createAccessor().loadFile(presentation, ""));
    }
}
