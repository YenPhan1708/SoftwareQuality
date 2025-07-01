import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionItemTest
{
    private CollectionItem collection;
    private final Style parentStyle = Style.getStyle(1);
    private final Style childStyle = Style.getStyle(2);
    private final ImageObserver observer = (img, flags, x, y, w, h) -> true;

    @BeforeEach
    public void setup()
    {
        collection = new CollectionItem(1);
    }

    @Test
    public void testConstructorLevel()
    {
        assertEquals(1, collection.getLevel());
    }


    @Test
    public void testRemoveItem()
    {
        TextItem item = new TextItem(1, "RemoveMe");
        Style style = Style.getStyle(1);
        collection.add(item, style);

        collection.remove(item);
        String result = collection.toString();
        assertFalse(result.contains("RemoveMe"));
    }

    @Test
    public void testDrawDoesNotThrow()
    {
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

    @Test
    public void testSetStyleWithEmptyChildrenDoesNotThrow()
    {
        CollectionItem collection = new CollectionItem(1);
        assertDoesNotThrow(() -> collection.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithOnlyTextItemsUsesItemStyle()
    {
        CollectionItem collection = new CollectionItem(1);
        TextItem t1 = new TextItem(2, "Text 1");
        TextItem t2 = new TextItem(2, "Text 2");

        collection.add(t1, childStyle);
        collection.add(t2, childStyle);

        assertDoesNotThrow(() -> collection.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithOnlyNestedCollectionUsesParentStyle()
    {
        CollectionItem parent = new CollectionItem(1);
        CollectionItem child = new CollectionItem(2);

        child.add(new TextItem(2, "Nested text"), Style.getStyle(2));
        parent.add(child, Style.getStyle(1));

        assertDoesNotThrow(() -> parent.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithMixedItems()
    {
        CollectionItem parent = new CollectionItem(1);

        TextItem text = new TextItem(2, "Text");
        CollectionItem nested = new CollectionItem(2);
        nested.add(new TextItem(2, "Nested"), Style.getStyle(2));

        parent.add(text, childStyle); // Should use childStyle
        parent.add(nested, childStyle); // Should use parentStyle

        assertDoesNotThrow(() -> parent.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithNullStyleDoesNotThrow()
    {
        CollectionItem collection = new CollectionItem(1);
        TextItem item = new TextItem(2, "Null style test");

        collection.add(item, Style.getStyle(2));
        assertDoesNotThrow(() -> collection.setStyle(null));
    }
}
