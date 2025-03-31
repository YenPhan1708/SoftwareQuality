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
}
