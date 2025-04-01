import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionItemTest {

    private CollectionItem collection;

    @BeforeEach
    public void setup()
    {
        collection = new CollectionItem(1);
    }

    @Test
    public void testConstructorLevel() {
        assertEquals(1, collection.getLevel());
    }

    @Test
    public void testAddWithStyleAffectsToString() {
        TextItem textItem = new TextItem(1, "Hello");
        Style style = Style.getStyle(1);

        collection.add(textItem, style);
        String result = collection.toString();

        assertTrue(result.contains("CollectionItem")); // from toString()
        assertTrue(result.contains("Hello")); // from TextItem’s toString()
    }

    @Test
    public void testRemoveItem() {
        TextItem item = new TextItem(1, "RemoveMe");
        Style style = Style.getStyle(1);
        collection.add(item, style);

        collection.remove(item);
        String result = collection.toString();
        assertFalse(result.contains("RemoveMe"));
    }

    @Test
    public void testDrawDoesNotThrow() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(1);

        TextItem item = new TextItem(1, "Draw me");
        collection.add(item, style);

        assertDoesNotThrow(() -> collection.draw(0, 0, 1.0f, g, style, observer));
    }

    @Test
    public void testBoundingBoxReturnsNonNull()
    {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(1);

        TextItem item = new TextItem(1, "SizeTest");
        collection.add(item, style);

        Rectangle box = collection.getBoundingBox(g, observer, 1.0f, style);
        assertNotNull(box);
        assertTrue(box.width >= 0);
        assertTrue(box.height >= 0);
    }

    @Test
    public void testSetStyleDoesNotThrow()
    {
        Style style = Style.getStyle(1);

        TextItem textItem = new TextItem(1, "Styled");
        collection.add(textItem, style);

        assertDoesNotThrow(() -> collection.setStyle(style));
    }
}
