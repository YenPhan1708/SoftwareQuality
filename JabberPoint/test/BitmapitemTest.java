import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class BitmapitemTest
{

    private BitmapItem bitmapItem;

    @BeforeEach
    public void setup() {
        bitmapItem = new BitmapItem();

        bitmapItem.setImageName("serclogo_fc.jpg");

        // simulate loading
        BufferedImage dummyImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        bitmapItem.setBufferedImage(dummyImage);
        bitmapItem.setLevel(2);
    }

    @Test
    public void testToStringContainsClassAndFilename() {
        String result = bitmapItem.toString();
        assertTrue(result.contains("BitmapItem"));
        assertTrue(result.contains("serclogo_fc.jpg"));
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
    public void testDrawDoesNotThrow()
    {
        Graphics dummyGraphics = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver dummyObserver =(img, flags, x, y, w, h)->true;
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> bitmapItem.draw(10, 10, 1.0f, dummyGraphics, style, dummyObserver));
    }

    @Test
    public void testImageNameCanBeNull()
    {
        BitmapItem item = new BitmapItem();
        item.setImageName(null);
        assertNull(item.getImageName());
        assertTrue(item.toString().contains("BitmapItem"));
    }

    @Test
    public void testBufferedImageCanBeNull()
    {
        BitmapItem item = new BitmapItem();
        item.setBufferedImage(null);
        assertNull(item.getBufferedImage());
    }

    @Test
    public void testGetBoundingBoxWithNullImageReturnsZeroBox()
    {
        BitmapItem item = new BitmapItem();
        item.setBufferedImage(null); // no image

        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(1);

        Rectangle box = item.getBoundingBox(g, null, 1.0f, style);
        assertEquals(0, box.width);
        assertEquals(0, box.height);
    }

    @Test
    public void testDrawWithNullObserverDoesNotThrow() {
        BitmapItem item = new BitmapItem();
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));

        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(1);

        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, style, null));
    }

    @Test
    public void testDrawWithNullGraphicsThrowsNullPointerException()
    {
        BitmapItem item = new BitmapItem();
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        Style style = Style.getStyle(1);
        ImageObserver observer = (img, flags, x, y, w, h) -> true;

        assertThrows(NullPointerException.class, () -> item.draw(0, 0, 1.0f, null, style, observer));
    }

    @Test
    public void testDrawWithNullStyleDoesNotThrow()
    {
        BitmapItem item = new BitmapItem();
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;

        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, null, observer));
    }
}
