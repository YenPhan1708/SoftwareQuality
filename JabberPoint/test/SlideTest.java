import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.lang.reflect.Method;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class SlideTest {

    private Slide slide;

    static class TestObserver implements SlideObserver {
        boolean updated = false;
        @Override
        public void update(Slide slide) {
            updated = true;
        }
    }

    @BeforeEach
    public void setup() {
        slide = new Slide();
    }

    @Test
    public void testSetAndGetTitle() {
        slide.setTitle("Test Slide");
        assertEquals("Test Slide", slide.getTitle());
    }

    @Test
    public void testAppendTextItem() {
        slide.append(1, "Test message");
        assertEquals(1, slide.getSize());

        SlideItem item = slide.getSlideItem(0);
        assertTrue(item instanceof TextItem);
        assertEquals(1, item.getLevel());
        assertTrue(((TextItem) item).getText().contains("Test"));
    }

    @Test
    public void testAppendSlideItem() {
        TextItem item = new TextItem(2, "Hello");
        slide.append(item);
        assertEquals(1, slide.getSize());
        assertEquals(item, slide.getSlideItem(0));
    }

    @Test
    public void testGetSlideItemsReturnsVector() {
        slide.append(1, "One");
        slide.append(2, "Two");

        Vector<SlideItem> items = slide.getSlideItems();
        assertEquals(2, items.size());
    }

    @Test
    public void testObserverNotifications() {
        TestObserver observer = new TestObserver();
        slide.addObserver(observer);

        slide.setTitle("Triggers notification");
        assertTrue(observer.updated);

        slide.removeObserver(observer);
        observer.updated = false;

        slide.setTitle("No more notify");
        assertFalse(observer.updated);
    }

    @Test
    public void testDrawDoesNotThrow() {
        Style.createStyles();
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, infoflags, x, y, width, height) -> true;
        Rectangle area = new Rectangle(0, 0, 1200, 800);
        slide.setTitle("Draw Test");
        slide.append(1, "Item A");
        slide.append(2, "Item B");
        assertDoesNotThrow(() -> slide.draw(g, area, observer));
    }


    @Test
    public void testGetScaleReflection() throws Exception {
        Rectangle area = new Rectangle(0, 0, 1200, 800);
        Method m = Slide.class.getDeclaredMethod("getScale", Rectangle.class);
        m.setAccessible(true);

        float scale = (float) m.invoke(slide, area);
        assertEquals(1.0f, scale, 0.01f);
    }

    @Test
    public void testAppendNullSlideItem() {
        Slide slide = new Slide();
        assertDoesNotThrow(() -> slide.append(null));
        assertEquals(1, slide.getSize());
        assertNull(slide.getSlideItem(0));
    }

    @Test
    public void testAppendTextWithNullText() {
        Slide slide = new Slide();
        assertDoesNotThrow(() -> slide.append(1, null));
        SlideItem item = slide.getSlideItem(0);
        assertTrue(item instanceof TextItem);
        assertEquals("", ((TextItem) item).getText());
    }

    @Test
    public void testAppendWithNegativeLevel() {
        Slide slide = new Slide();
        slide.append(-1, "Negative level test");
        SlideItem item = slide.getSlideItem(0);
        assertEquals(-1, item.getLevel());
    }

    @Test
    public void testGetSlideItemWithNegativeIndexThrows() {
        Slide slide = new Slide();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> slide.getSlideItem(-1));
    }


    @Test
    public void testGetSlideItemOutOfBoundsThrowsException() {
        Slide slide = new Slide();
        slide.append(1, "Item A");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> slide.getSlideItem(2));
    }


    @Test
    public void testSetTitleToNull() {
        Slide slide = new Slide();
        assertDoesNotThrow(() -> slide.setTitle(null));
        assertNull(slide.getTitle());
    }

    @Test
    public void testDrawWithNullObserverDoesNotThrow() {
        Style.createStyles();
        Slide slide = new Slide();
        slide.setTitle("Draw me");
        slide.append(1, "Some content");

        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Rectangle area = new Rectangle(0, 0, 800, 600);
        assertDoesNotThrow(() -> slide.draw(g, area, null));
    }

    @Test
    public void testDrawWithNoItemsDoesNotThrow() {
        Style.createStyles();
        Slide slide = new Slide();
        slide.setTitle("Empty slide");
        Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Rectangle area = new Rectangle(0, 0, 800, 600);
        assertDoesNotThrow(() -> slide.draw(g, area, (img, f, x, y, w, h) -> true));
    }


    @Test
    public void testAddSameObserverTwiceDoesNotCrash() {
        Slide slide = new Slide();
        SlideObserver observer = s -> {};
        slide.addObserver(observer);
        slide.addObserver(observer);
        assertDoesNotThrow(() -> slide.setTitle("Triggers notify"));
    }

    @Test
    public void testRemoveUnregisteredObserverDoesNotCrash() {
        Slide slide = new Slide();
        SlideObserver observer = s -> {};
        assertDoesNotThrow(() -> slide.removeObserver(observer));
    }

    @Test
    public void testGetScaleHandlesVerySmallArea() throws Exception {
        Slide slide = new Slide();
        Method m = Slide.class.getDeclaredMethod("getScale", Rectangle.class);
        m.setAccessible(true);
        Rectangle smallArea = new Rectangle(0, 0, 10, 10);
        float result = (float) m.invoke(slide, smallArea);
        assertTrue(result > 0);
    }
}
