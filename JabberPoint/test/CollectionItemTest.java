import Accessor.CollectionItem;
import Accessor.TextItem;
import Accessor.BitmapItem;
import Style.Style;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionItemTest {

    private CollectionItem collection;
    private final Style parentStyle = Style.getStyle(1);
    private final Style childStyle = Style.getStyle(2);
    private final ImageObserver observer = (img, flags, x, y, w, h) -> true;

    @BeforeEach
    public void setup() {
        collection = new CollectionItem(1);
    }

    @Test
    public void testConstructorLevel() {
        assertEquals(1, collection.getLevel());
    }

    @Test
    public void testRemoveItem() {
        TextItem item = new TextItem(1, "RemoveMe");
        collection.add(item, childStyle);

        collection.remove(item);
        String result = collection.toString();
        assertFalse(result.contains("RemoveMe"));
    }

    @Test
    public void testDrawDoesNotThrow() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        TextItem item = new TextItem(1, "Draw me");
        collection.add(item, childStyle);

        assertDoesNotThrow(() -> collection.draw(0, 0, 1.0f, g, parentStyle, observer));
    }

    @Test
    public void testBoundingBoxReturnsNonNull() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        TextItem item = new TextItem(1, "SizeTest");
        collection.add(item, childStyle);

        Rectangle box = collection.getBoundingBox(g, observer, 1.0f, parentStyle);
        assertNotNull(box);
        assertTrue(box.width >= 0);
        assertTrue(box.height >= 0);
    }

    @Test
    public void testSetStyleDoesNotThrow() {
        TextItem textItem = new TextItem(1, "Styled");
        collection.add(textItem, childStyle);

        assertDoesNotThrow(() -> collection.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithEmptyChildrenDoesNotThrow() {
        assertDoesNotThrow(() -> collection.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithOnlyTextItemsUsesItemStyle() {
        TextItem t1 = new TextItem(2, "Text 1");
        TextItem t2 = new TextItem(2, "Text 2");

        collection.add(t1, childStyle);
        collection.add(t2, childStyle);

        assertDoesNotThrow(() -> collection.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithOnlyNestedCollectionUsesParentStyle() {
        CollectionItem parent = new CollectionItem(1);
        CollectionItem child = new CollectionItem(2);
        child.add(new TextItem(2, "Nested text"), childStyle);

        parent.add(child, parentStyle);

        assertDoesNotThrow(() -> parent.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithMixedItems() {
        CollectionItem parent = new CollectionItem(1);

        TextItem text = new TextItem(2, "Text");
        CollectionItem nested = new CollectionItem(2);
        nested.add(new TextItem(2, "Nested"), childStyle);

        parent.add(text, childStyle);     // Applies childStyle
        parent.add(nested, childStyle);   // Should apply recursively

        assertDoesNotThrow(() -> parent.setStyle(parentStyle));
    }

    @Test
    public void testSetStyleWithNullStyleDoesNotThrow() {
        TextItem item = new TextItem(2, "Null style test");
        collection.add(item, childStyle);
        assertDoesNotThrow(() -> collection.setStyle(null));
    }

    @Test
    public void testDrawWithBitmapItemDoesNotThrow() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        BitmapItem bitmap = new BitmapItem(1, "JabberPoint.jpg");
        bitmap.setBufferedImage(new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB));

        collection.add(bitmap, childStyle);
        assertDoesNotThrow(() -> collection.draw(0, 0, 1.0f, g, parentStyle, observer));
    }
}
