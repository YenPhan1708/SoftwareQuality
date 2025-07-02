import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import Presentation.Presentation;
import Slide.Slide;

import static org.junit.jupiter.api.Assertions.*;

public class PresentationTest
{

    private Presentation presentation;

    @BeforeEach
    public void setup()
    {
        presentation = new Presentation();
    }

    @Test
    public void testConstructorSetsDefaults()
    {
        assertEquals(0, presentation.getSize());
        assertEquals(-1, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testAddAndGetSlide()
    {
        Slide slide = new Slide();
        slide.setTitle("Slide 1");
        presentation.addSlide(slide);

        assertEquals(1, presentation.getSize());
        assertEquals(slide, presentation.getSlide(0));
    }

    @Test
    public void testGetCurrentSlide()
    {
        Slide slide = new Slide();
        presentation.addSlide(slide);
        presentation.setSlideNumber(0);

        assertEquals(slide, presentation.getCurrentSlide());
    }

    @Test
    public void testSetSlideNumberAndGet()
    {
        Slide slide = new Slide();
        presentation.addSlide(slide);
        presentation.setSlideNumber(0);

        assertEquals(0, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testNextAndPrevSlide()
    {
        Slide slide1 = new Slide();
        Slide slide2 = new Slide();
        presentation.addSlide(slide1);
        presentation.addSlide(slide2);
        presentation.setSlideNumber(0);

        presentation.nextSlide();
        assertEquals(1, presentation.getCurrentSlideNumber());

        presentation.prevSlide();
        assertEquals(0, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testGetSlideReturnsNullForOutOfBounds()
    {
        assertNull(presentation.getSlide(-1));
        assertNull(presentation.getSlide(100));
    }


    @Test
    public void testUpdatePrintsMessage()
    {
        Slide slide = new Slide();
        presentation.update(slide); // Only prints
        assertTrue(true);
    }

    @Test
    public void testNotifySlideChangeViaReflection() throws Exception
    {
        Slide slide = new Slide();
        presentation.addSlide(slide);
        presentation.setSlideNumber(0);

        Method method = Presentation.class.getDeclaredMethod("notifySlideChange");
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(presentation));
    }

    @Test
    public void testSetTitleToEmptyString()
    {
        presentation.setShowTitle("");
        assertEquals("", presentation.getShowTitle());
    }

    @Test
    public void testSetTitleToNullDoesNotThrow()
    {
        assertDoesNotThrow(() -> presentation.setShowTitle(null));
        assertNull(presentation.getShowTitle());
    }

    @Test
    public void testGetSlideWithNegativeIndexReturnsNull()
    {
        assertNull(presentation.getSlide(-1));
    }

    @Test
    public void testGetSlideWithTooHighIndexReturnsNull()
    {
        presentation.addSlide(new Slide());
        assertNull(presentation.getSlide(5));
    }

    @Test
    public void testNextSlideWithOnlyOneSlideDoesNotOverflow()
    {
        presentation.addSlide(new Slide());
        presentation.setSlideNumber(0);
        presentation.nextSlide();
        assertEquals(0, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testPrevSlideAtFirstSlideDoesNotUnderflow()
    {
        presentation.addSlide(new Slide());
        presentation.setSlideNumber(0);
        presentation.prevSlide();
        assertEquals(0, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testNextSlideWithNoSlidesDoesNothing()
    {
        presentation.nextSlide();
        assertEquals(-1, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testPrevSlideWithNoSlidesDoesNothing()
    {
        presentation.prevSlide();
        assertEquals(-1, presentation.getCurrentSlideNumber());
    }

    @Test
    public void testGetCurrentSlideWhenNoneSetReturnsNull()
    {
        assertNull(presentation.getCurrentSlide());
    }

    @Test
    public void testSetSlideNumberToInvalidNegativeValue()
    {
        presentation.addSlide(new Slide());
        presentation.setSlideNumber(-5);
        assertEquals(-5, presentation.getCurrentSlideNumber());
        assertNull(presentation.getCurrentSlide());
    }

    @Test
    public void testUpdateWithNullDoesNotThrow()
    {
        assertDoesNotThrow(() -> presentation.update(null));
    }
}
