import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

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
    @Test
    public void testMultipleObserversAreCalled() {
        Slide slide = new Slide();

        AtomicInteger callCount = new AtomicInteger(0);

        SlideObserver obs1 = s -> callCount.incrementAndGet();
        SlideObserver obs2 = s -> callCount.incrementAndGet();

        slide.addObserver(obs1);
        slide.addObserver(obs2);

        slide.setTitle("Notify all");

        assertEquals(2, callCount.get());
    }

    @Test
    public void testSameObserverCalledTwiceIfAddedTwice() {
        Slide slide = new Slide();
        AtomicInteger callCount = new AtomicInteger(0);

        SlideObserver observer = s -> callCount.incrementAndGet();
        slide.addObserver(observer);
        slide.addObserver(observer);

        slide.setTitle("Trigger");

        assertEquals(2, callCount.get());
    }

    @Test
    public void testRemoveObserverMultipleTimesIsSafe() {
        Slide slide = new Slide();
        SlideObserver observer = s -> {};
        slide.addObserver(observer);

        slide.removeObserver(observer);
        assertDoesNotThrow(() -> slide.removeObserver(observer)); // no crash
    }

    @Test
    public void testObserverReceivesUpdatedSlideState() {
        Slide slide = new Slide();

        SlideObserver observer = s -> assertEquals("Live Update", s.getTitle());
        slide.addObserver(observer);

        slide.setTitle("Live Update");
    }

    @Test
    public void testObserverThrowingExceptionDoesNotCrashNotify() {
        Slide slide = new Slide();
        SlideObserver badObserver = s -> { throw new RuntimeException("oops"); };
        SlideObserver goodObserver = s -> assertTrue(true);

        slide.addObserver(badObserver);
        slide.addObserver(goodObserver);

        // Simulate safe notify without crash
        assertDoesNotThrow(() -> slide.setTitle("Safe notify"));
    }

}

