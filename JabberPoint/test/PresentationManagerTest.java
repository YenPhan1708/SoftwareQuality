import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PresentationManagerTest {

    private TestPresentationManager manager;
    private Presentation presentation;

    static class DummyAccessor implements Accessor
    {
        boolean loaded = false;
        boolean saved = false;

        @Override
        public void loadFile(Presentation p, String filename) throws IOException
        {
            if (p == null || filename == null || filename.isEmpty())
            {
                throw new NullPointerException("Filename must not be null or empty");
            }
            loaded = true;
        }

        @Override
        public void saveFile(Presentation p, String filename) throws IOException
        {
            if (p == null || filename == null || filename.isEmpty())
            {
                throw new NullPointerException("Filename must not be null or empty");
            }
            saved = true;
        }

    }

    static class TestPresentationManager extends PresentationManager
    {
        DummyAccessor dummyAccessor;

        public TestPresentationManager(Presentation presentation)
        {
            super(presentation);
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
        manager = new TestPresentationManager(presentation);
    }

    @Test
    public void testGetAndSetPresentation()
    {
        assertEquals(presentation, manager.getPresentation());

        Presentation newPres = new Presentation();
        manager.setPresentation(newPres);
        assertEquals(newPres, manager.getPresentation());
    }

    @Test
    public void testLoadFileCallsAccessor() throws IOException
    {
        manager.loadFile(presentation,"test.xml");
        assertTrue(manager.getDummyAccessor().loaded);
    }

    @Test
    public void testSaveFileCallsAccessor() throws IOException
    {
        manager.saveFile(presentation, "test.xml");
        assertTrue(manager.getDummyAccessor().saved);
    }

    @Test
    public void testLoadFileWithNullFilenameThrows()
    {
        Presentation p = new Presentation();
        TestPresentationManager manager = new TestPresentationManager(p);
        assertThrows(NullPointerException.class, () -> manager.loadFile(presentation, null));
    }

    @Test
    public void testSaveFileWithNullFilenameThrows()
    {
        Presentation p = new Presentation();
        TestPresentationManager manager = new TestPresentationManager(p);
        assertThrows(NullPointerException.class, () -> manager.saveFile(presentation, null));
    }

    @Test
    public void testLoadFileWithNullPresentationThrows()
    {
        TestPresentationManager manager = new TestPresentationManager(null);
        assertThrows(NullPointerException.class, () -> manager.loadFile(presentation,null));
    }

    @Test
    public void testSaveFileWithNullPresentationThrows()
    {
        TestPresentationManager manager = new TestPresentationManager(null);

        assertThrows(NullPointerException.class, () -> manager.saveFile(null, "file.xml"));
    }

    @Test
    public void testCreateAccessorReturnsSameInstance()
    {
        TestPresentationManager manager = new TestPresentationManager(new Presentation());

        Accessor first = manager.createAccessor();
        Accessor second = manager.createAccessor();

        assertSame(first, second, "Accessor should be cached and reused");
    }

    @Test
    public void testSaveFileWithEmptyFilenameThrows()
    {
        Presentation p = new Presentation();
        TestPresentationManager manager = new TestPresentationManager(p);

        assertThrows(NullPointerException.class, () -> manager.saveFile(p, ""));
    }

    @Test
    public void testLoadFileWithEmptyFilenameThrows()
    {
        Presentation p = new Presentation();
        TestPresentationManager manager = new TestPresentationManager(p);
        assertThrows(NullPointerException.class, () -> manager.loadFile(p, ""));
    }

}

