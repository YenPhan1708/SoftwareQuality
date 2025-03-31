import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class BitmapitemTest {

    private BitmapItem bitmapItem;
    private BufferedImage dummyImage;

    @BeforeEach
    public void setup() {
        bitmapItem = new BitmapItem();
        dummyImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        bitmapItem.setBufferedImage(dummyImage);
        bitmapItem.setImageName("test.png");
        bitmapItem.setLevel(2);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals("test.png", bitmapItem.getImageName());
        assertEquals(dummyImage, bitmapItem.getBufferedImage());
        assertEquals(2, bitmapItem.getLevel());
    }

    @Test
    public void testToString() {
        String result = bitmapItem.toString();
        assertTrue(result.contains("BitmapItem"));
        assertTrue(result.contains("test.png"));
    }

    @Test
    public void testGetBoundingBox() {
        ImageObserver dummyObserver = (img, infoflags, x, y, width, height) -> true;
        Graphics dummyGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(2);

        Rectangle box = bitmapItem.getBoundingBox(dummyGraphics, dummyObserver, 1.0f, style);

        assertEquals((int) (style.indent * 1.0f), box.x);
        assertEquals(100, box.width);
        assertEquals(style.leading + 50, box.height);
    }

    @Test
    public void testDrawDoesNotThrow() {
        ImageObserver dummyObserver = (img, infoflags, x, y, width, height) -> true;
        Graphics dummyGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> bitmapItem.draw(10, 10, 1.0f, dummyGraphics, style, dummyObserver));
    }
}
