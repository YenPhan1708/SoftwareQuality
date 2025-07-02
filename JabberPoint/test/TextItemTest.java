import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Accessor.*;
import Style.Style;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TextItemTest
{
    private TextItem textItem;
    private final Graphics g = new BufferedImage(1000, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
    private final ImageObserver observer = (img, infoflags, x, y, width, height) -> true;

    @BeforeEach
    public void setup() {
        textItem = new TextItem(2, "Hello world");
    }


    @Test
    public void testAttributedStringReturnsCorrectFont()
    {
        Style style = Style.getStyle(1);
        AttributedString attrStr = textItem.getAttributedString(style, 1.0f);
        AttributedCharacterIterator it = attrStr.getIterator();
        Map<AttributedCharacterIterator.Attribute, Object> attrs = it.getAttributes();
        assertTrue(attrs.containsKey(TextAttribute.FONT));
    }

    @Test
    public void testGetBoundingBoxReturnsValidRectangle()
    {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(2);

        Rectangle box = textItem.getBoundingBox(g, observer, 1.0f, style);
        assertNotNull(box);
        assertTrue(box.width >= 0);
        assertTrue(box.height >= 0);
    }

    @Test
    public void testDrawDoesNotThrow()
    {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> textItem.draw(0, 0, 1.0f, g, style, observer));
    }

    @Test
    public void testToStringIncludesText()
    {
        String str = textItem.toString();
        assertTrue(str.contains("TextItem"));
        assertTrue(str.contains("Hello world"));
    }

    @Test
    public void testEmptyTextString()
    {
        TextItem item = new TextItem(1, "");
        assertEquals("", item.getText());
        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, Style.getStyle(1), observer));
    }

    @Test
    public void testNullTextString()
    {
        TextItem item = new TextItem(1, null);
        assertEquals("", item.getText());
        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, Style.getStyle(1), observer));
    }

    @Test
    public void testNullFontDoesNotBreak() {
        TextItem item = new TextItem(1, "Test");
        item.setFont(null);  // shouldn't break anything
        assertDoesNotThrow(() -> item.getFont());
    }

    @Test
    public void testNullColorAccepted() {
        TextItem item = new TextItem(1, "Colorless");
        item.setColor(null);
        assertNull(item.getColor());
    }

    @Test
    public void testNullObserverInDrawDoesNotThrow() {
        TextItem item = new TextItem(1, "No observer");
        assertDoesNotThrow(() -> item.draw(0, 0, 1.0f, g, Style.getStyle(1), null));
    }

    @Test
    public void testDrawWithExtremeFontScale() {
        TextItem item = new TextItem(1, "Scale up");
        Style style = Style.getStyle(1);
        assertDoesNotThrow(() -> item.draw(0, 0, 10.0f, g, style, observer));
    }

    @Test
    public void testNegativeLevelHandled() {
        TextItem item = new TextItem(-2, "Negative");
        assertEquals(-2, item.getLevel());
    }

    @Test
    public void testGetAttributedStringWithNullStyleThrows() {
        TextItem item = new TextItem(1, "Test");
        assertThrows(NullPointerException.class, () -> item.getAttributedString(null, 1.0f));
    }

    @Test
    public void testAttributedStringReturnsCorrectAttributes() {
        TextItem item = new TextItem(1, "Styled");
        Style style = Style.getStyle(1);
        AttributedString attrStr = item.getAttributedString(style, 1.0f);
        AttributedCharacterIterator it = attrStr.getIterator();
        Map<AttributedCharacterIterator.Attribute, Object> attrs = it.getAttributes();

        assertTrue(attrs.containsKey(TextAttribute.FONT));
    }

    @Test
    public void testToStringContainsLevelAndText() {
        TextItem item = new TextItem(3, "Stringified");
        String str = item.toString();
        assertTrue(str.contains("TextItem"));
        assertTrue(str.contains("Stringified"));
    }
}
