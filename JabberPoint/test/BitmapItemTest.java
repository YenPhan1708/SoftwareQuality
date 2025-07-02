import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Accessor.*;
import Style.Style;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class BitmapItemTest
{

    private BitmapItem bitmapItem;

    @BeforeEach
    public void setup()
    {
        // Create BitmapItem with mock image name and override image manually
        bitmapItem = new BitmapItem(2, "JabberPoint.jpg");
        bitmapItem.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
    }

    @Test
    public void testToStringContainsClassAndFilename()
    {
        String result = bitmapItem.toString();
        assertTrue(result.contains("BitmapItem"));
        assertTrue(result.contains("JabberPoint.jpg"));
    }

    @Test
    public void testBoundingBoxCalculation()
    {
        Style.createStyles();

        ImageObserver dummyObserver = (img, flags, x, y, w, h) -> true;
        Graphics dummyGraphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(2);

        Rectangle box = bitmapItem.getBoundingBox(dummyGraphics, dummyObserver, 1.0f, style);
        assertEquals((int)(style.indent * 1.0f), box.x);
        assertEquals(100, box.width); // mocked image width
        assertEquals(style.leading + 50, box.height); // mocked image height
    }

    @Test
    public void testDrawDoesNotThrow() {
        Style.createStyles();

        Graphics dummyGraphics = new BufferedImage(200, 100, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver dummyObserver = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> bitmapItem.draw(10, 10, 1.0f, dummyGraphics, style, dummyObserver));
    }

    @Test
    public void testImageNameCanBeNull() {
        BitmapItem item = new BitmapItem(0, "JabberPoint.jpg");
        item.setImageName(null);

        assertNull(item.getImageName());
        assertTrue(item.toString().contains("BitmapItem"));
    }

    @Test
    public void testBufferedImageCanBeNull() {
        BitmapItem item = new BitmapItem(0, "JabberPoint.jpg");
        item.setBufferedImage(null);
        assertNull(item.getBufferedImage());
    }

    @Test
    public void testGetBoundingBoxWithNullImageThrows() {
        Style.createStyles();
        BitmapItem item = new BitmapItem(1, "JabberPoint.jpg");
        item.setBufferedImage(null);
        Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(1);
        ImageObserver observer = (img, flags, x, y, width, height) -> true;

        assertThrows(NullPointerException.class, () ->
                item.getBoundingBox(g, observer, 1.0f, style));
    }

    @Test
    public void testDrawWithNullObserverDoesNotThrow() {
        Style.createStyles();

        BitmapItem item = new BitmapItem(1, "JabberPoint.jpg");
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        Style style = Style.getStyle(1);

        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, style, null));
    }

    @Test
    public void testDrawWithNullGraphicsThrowsNullPointerException() {
        Style.createStyles();

        BitmapItem item = new BitmapItem(1, "JabberPoint.jpg");
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        Style style = Style.getStyle(1);
        ImageObserver observer = (img, flags, x, y, w, h) -> true;

        assertThrows(NullPointerException.class, () -> item.draw(0, 0, 1.0f, null, style, observer));
    }

    @Test
    public void testDrawWithNullStyleThrows() {
        BitmapItem item = new BitmapItem(1, "JabberPoint.jpg");
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        Graphics g = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;

        assertThrows(NullPointerException.class, () -> item.draw(0, 0, 1.0f, g, null, observer));
    }
}