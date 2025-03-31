import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SlideObserverTest {

    private Slide slide;
    private TestObserver observer;

    static class TestObserver implements SlideObserver {
        boolean updated = false;

        @Override
        public void update(Slide slide) {
            updated = true;
        }

        public boolean isUpdated() {
            return updated;
        }
    }

    @BeforeEach
    public void setup() {
        slide = new Slide();
        observer = new TestObserver();
    }

    @Test
    public void testObserverIsCalledOnSlideUpdate() {
        slide.addObserver(observer);

        slide.setTitle("Updated Title");

        assertTrue(observer.isUpdated(), "Observer should be notified when slide updates");
    }

    @Test
    public void testObserverNotCalledAfterRemoved() {
        slide.addObserver(observer);
        slide.removeObserver(observer);

        slide.setTitle("No update");

        assertFalse(observer.isUpdated(), "Removed observer should not be notified");
    }
}

