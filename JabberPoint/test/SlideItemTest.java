import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class SlideItemTest
{

    private Graphics graphics;
    private ImageObserver observer;
    private Style style;

    @BeforeEach
    public void setup()
    {
        Style.createStyles();
        graphics = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        observer = (img, infoflags, x, y, width, height) -> true;
        style = Style.getStyle(1);
    }


    @Test
    public void testTextItemAsSlideItem()
    {
        SlideItem item = new TextItem(1, "SlideItem Text");
        assertEquals(1, item.getLevel());

        Rectangle box = item.getBoundingBox(graphics, observer, 1.0f, style);
        assertNotNull(box);

        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, graphics, style, observer));
        assertDoesNotThrow(() -> item.setStyle(style));
    }

    @Test
    public void testBitmapItemAsSlideItem()
    {
        BitmapItem item = new BitmapItem(2, "serclogo_fc.jpg");
        item.setImageName("serclogo_fc.jpg");
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        item.setLevel(2);

        assertEquals(2, item.getLevel());

        Rectangle box = item.getBoundingBox(graphics, observer, 1.0f, style);
        assertNotNull(box);

        assertDoesNotThrow(() -> item.draw(10, 10, 1.0f, graphics, style, observer));
        assertDoesNotThrow(() -> item.setStyle(style));
    }

    @Test
    public void testCollectionItemAsSlideItem()
    {
        CollectionItem item = new CollectionItem(3);
        item.add(new TextItem(1, "Nested"), Style.getStyle(1));

        assertEquals(3, item.getLevel());

        Rectangle box = item.getBoundingBox(graphics, observer, 1.0f, style);
        assertNotNull(box);

        assertDoesNotThrow(() -> item.draw(5, 5, 1.0f, graphics, style, observer));
        assertDoesNotThrow(() -> item.setStyle(style));
    }

    @Test
    public void testTextItemWithNullText()
    {
        TextItem item = new TextItem(1, null);
        assertEquals("", item.getText());
        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, graphics, Style.getStyle(1), observer));
    }


    @Test
    public void testTextItemWithNegativeLevel()
    {
        TextItem item = new TextItem(-3, "Negative level");
        assertEquals(-3, item.getLevel());
    }

    @Test
    public void testTextItemWithHugeLevel()
    {
        TextItem item = new TextItem(99, "Extreme level");
        assertEquals(99, item.getLevel());
    }

    @Test
    public void testBitmapItemWithNullBufferedImageThrows()
    {
        BitmapItem item = new BitmapItem(2, "serclogo_fc.jpg");
        item.setBufferedImage(null);
        assertThrows(NullPointerException.class, () ->
                item.draw(0, 0, 1.0f, graphics, Style.getStyle(2), observer)
        );
    }


    @Test
    public void testBitmapItemWithHugeScale()
    {
        BitmapItem item = new BitmapItem(1, "serclogo_fc.jpg");
        item.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));
        assertDoesNotThrow(() -> item.draw(
                0,
                0,
                10.0f,
                graphics,
                Style.getStyle(1),
                observer
        ));
    }


    @Test
    public void testCollectionItemWithNestedLevels()
    {
        CollectionItem parent = new CollectionItem(1);
        CollectionItem nested = new CollectionItem(2);

        nested.add(new TextItem(2, "Nested item"), Style.getStyle(2));
        parent.add(nested, Style.getStyle(1));

        assertDoesNotThrow(() -> parent.draw(0, 0, 1.0f, graphics, Style.getStyle(1), observer));
    }

    @Test
    public void testCollectionItemWithMixedChildren()
    {
        CollectionItem collection = new CollectionItem(1);
        collection.add(new TextItem(1, "Text"), Style.getStyle(1));
        BitmapItem bitmap = new BitmapItem(1, "serclogo_fc.jpg");
        bitmap.setBufferedImage(new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB));

        collection.add(bitmap, Style.getStyle(1));

        assertDoesNotThrow(() -> collection.draw(0, 0, 1.0f, graphics, Style.getStyle(1), observer));
    }

}

