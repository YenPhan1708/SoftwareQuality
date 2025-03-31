import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class PresentationManagerTest {

    private TestPresentationManager manager;
    private Presentation presentation;

    static class DummyAccessor implements Accessor {
        boolean loaded = false;
        boolean saved = false;

        @Override
        public void loadFile(Presentation p, String filename) throws IOException {
            loaded = true;
        }

        @Override
        public void saveFile(Presentation p, String filename) throws IOException {
            saved = true;
        }
    }

    static class TestPresentationManager extends PresentationManager {
        DummyAccessor dummyAccessor;

        public TestPresentationManager(Presentation presentation) {
            super(presentation);
            this.dummyAccessor = new DummyAccessor();
        }

        @Override
        public Accessor createAccessor() {
            return dummyAccessor;
        }

        public DummyAccessor getDummyAccessor() {
            return dummyAccessor;
        }
    }

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
        manager = new TestPresentationManager(presentation);
    }

    @Test
    public void testGetAndSetPresentation() {
        assertEquals(presentation, manager.getPresentation());

        Presentation newPres = new Presentation();
        manager.setPresentation(newPres);
        assertEquals(newPres, manager.getPresentation());
    }

    @Test
    public void testLoadFileCallsAccessor() throws IOException {
        manager.loadFile("test.xml");
        assertTrue(manager.getDummyAccessor().loaded);
    }

    @Test
    public void testSaveFileCallsAccessor() throws IOException {
        manager.saveFile("test-output.xml");
        assertTrue(manager.getDummyAccessor().saved);
    }
}

