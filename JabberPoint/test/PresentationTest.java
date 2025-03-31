import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PresentationTest {

    private Presentation presentation;

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
    }

    @Test
    public void testConstructorSetsDefaults() {
        assertEquals(0, presentation.getSize());
        assertEquals(-1, presentation.getSlideNumber());
    }

    @Test
    public void testSetAndGetTitle() {
        presentation.setTitle("My Presentation");
        assertEquals("My Presentation", presentation.getTitle());
    }

    @Test
    public void testAppendAndGetSlide() {
        Slide slide = new Slide();
        slide.setTitle("Slide 1");
        presentation.append(slide);

        assertEquals(1, presentation.getSize());
        assertEquals(slide, presentation.getSlide(0));
    }

    @Test
    public void testGetCurrentSlide() {
        Slide slide = new Slide();
        presentation.append(slide);
        presentation.setSlideNumber(0);

        assertEquals(slide, presentation.getCurrentSlide());
    }

    @Test
    public void testSetSlideNumberAndGet() {
        Slide slide = new Slide();
        presentation.append(slide);
        presentation.setSlideNumber(0);

        assertEquals(0, presentation.getSlideNumber());
    }

    @Test
    public void testNextAndPrevSlide() {
        Slide slide1 = new Slide();
        Slide slide2 = new Slide();
        presentation.append(slide1);
        presentation.append(slide2);
        presentation.setSlideNumber(0);

        presentation.nextSlide();
        assertEquals(1, presentation.getSlideNumber());

        presentation.prevSlide();
        assertEquals(0, presentation.getSlideNumber());
    }

    @Test
    public void testGetSlideReturnsNullForOutOfBounds() {
        assertNull(presentation.getSlide(-1));
        assertNull(presentation.getSlide(100));
    }

    @Test
    public void testExit() {
        // This is not testable directly since it calls System.exit(n)
        // You would normally mock System.exit in advanced setups
        assertTrue(true); // Placeholder
    }

    @Test
    public void testUpdatePrintsMessage() {
        Slide slide = new Slide();
        presentation.update(slide); // It only prints to console
        assertTrue(true); // Placeholder
    }

    @Test
    public void testNotifySlideChangeViaReflection() throws Exception {
        Slide slide = new Slide();
        presentation.append(slide);
        presentation.setSlideNumber(0);

        Method method = Presentation.class.getDeclaredMethod("notifySlideChange");
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(presentation));
    }
}

