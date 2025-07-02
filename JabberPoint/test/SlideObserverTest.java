import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Slide.*;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SlideObserverTest {

    private Slide slide;
    private TestObserver observer;

    static class TestObserver implements SlideObserver {
        private boolean updated = false;

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
        assertTrue(observer.isUpdated(), "Observer should be notified on title change");
    }

    @Test
    public void testObserverNotCalledAfterRemoval() {
        slide.addObserver(observer);
        slide.removeObserver(observer);
        slide.setTitle("Should not trigger");
        assertFalse(observer.isUpdated(), "Removed observer should not be notified");
    }

    @Test
    public void testMultipleObserversAreNotified() {
        AtomicInteger counter = new AtomicInteger(0);

        SlideObserver o1 = s -> counter.incrementAndGet();
        SlideObserver o2 = s -> counter.incrementAndGet();

        slide.addObserver(o1);
        slide.addObserver(o2);
        slide.setTitle("Notify all");

        assertEquals(2, counter.get(), "Both observers should be called");
    }

    @Test
    public void testSameObserverCalledTwiceIfAddedTwice() {
        AtomicInteger counter = new AtomicInteger(0);
        SlideObserver o = s -> counter.incrementAndGet();

        slide.addObserver(o);
        slide.addObserver(o); // Added twice intentionally
        slide.setTitle("Trigger");

        assertEquals(2, counter.get(), "Observer should be called twice if added twice");
    }

    @Test
    public void testRemovingObserverTwiceIsSafe() {
        SlideObserver o = s -> {};
        slide.addObserver(o);

        slide.removeObserver(o);
        assertDoesNotThrow(() -> slide.removeObserver(o), "Removing same observer twice should be safe");
    }

    @Test
    public void testObserverReceivesCurrentSlideState() {
        SlideObserver o = s -> assertEquals("Live Update", s.getTitle());
        slide.addObserver(o);
        slide.setTitle("Live Update");
    }

    @Test
    public void testObserverExceptionIsCaughtAndDoesNotCrash() {
        SlideObserver bad = s -> { throw new RuntimeException("fail"); };
        SlideObserver good = s -> assertTrue(true);

        slide.addObserver(bad);
        slide.addObserver(good);

        assertDoesNotThrow(() -> slide.setTitle("Safe update"), "Exception from one observer should not crash others");
    }
}
