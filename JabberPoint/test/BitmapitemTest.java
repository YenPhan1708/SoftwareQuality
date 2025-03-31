import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class BitmapitemTest {

    private BitmapItem bitmapItem;

    @BeforeEach
    public void setup() {
        bitmapItem = new BitmapItem();
        bitmapItem.setImageName("test-image.png");

        // simulate loading
        BufferedImage dummyImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        bitmapItem.setBufferedImage(dummyImage);
        bitmapItem.setLevel(2);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("test.png", bitmapItem.getImageName());
        assertNull(bitmapItem.getBufferedImage());
        assertEquals(2, bitmapItem.getLevel());
    }

    @Test
    public void testToStringContainsClassAndFilename() {
        String result = bitmapItem.toString();
        assertTrue(result.contains("BitmapItem"));
        assertTrue(result.contains("test-image.png"));
    }

    @Test
    public void testBoundingBoxCalculation() {
        ImageObserver dummyObserver = (img, flags, x, y, w, h) -> true;
        Graphics dummyGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(2);

        Rectangle box = bitmapItem.getBoundingBox(dummyGraphics, dummyObserver, 1.0f, style);
        assertEquals((int)(style.indent * 1.0f), box.x);
        assertEquals(100, box.width); // image width
        assertEquals(style.leading + 50, box.height); // style leading + image height
    }

    @Test
    public void testDrawDoesNotThrow() {
        Graphics dummyGraphics = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver dummyObserver =(img, flags, x, y, w, h)->true;
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> bitmapItem.draw(10, 10, 1.0f, dummyGraphics, style, dummyObserver));
    }
}
