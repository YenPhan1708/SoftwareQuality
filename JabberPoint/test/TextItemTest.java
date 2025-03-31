import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TextItemTest {

    private TextItem textItem;

    @BeforeEach
    public void setup() {
        textItem = new TextItem(2, "Hello world");
    }

    @Test
    public void testConstructorAndGetText() {
        assertEquals("Hello world", textItem.getText());
        assertEquals(2, textItem.getLevel());
    }

    @Test
    public void testSetAndGetFont() {
        Font font = new Font("Arial", Font.PLAIN, 16);
        textItem.setFont(font);
        assertEquals(font, textItem.getFont());
    }

    @Test
    public void testSetAndGetColor() {
        Color color = Color.BLUE;
        textItem.setColor(color);
        assertEquals(color, textItem.getColor());
    }

    @Test
    public void testSetAndGetText() {
        textItem.setText("Updated");
        assertEquals("Updated", textItem.getText());
    }

    @Test
    public void testSetAndGetLevel() {
        textItem.setLevel(4);
        assertEquals(4, textItem.getLevel());
    }

    @Test
    public void testSetAndGetStyle() {
        Style style = Style.getStyle(2);
        textItem.setStyle(style);
        assertEquals(style, textItem.getStyle());
    }

    @Test
    public void testAttributedStringReturnsCorrectFont() {
        Style style = Style.getStyle(1);
        AttributedString attrStr = textItem.getAttributedString(style, 1.0f);
        AttributedCharacterIterator it = attrStr.getIterator();
        Map<AttributedCharacterIterator.Attribute, Object> attrs = it.getAttributes();
        assertTrue(attrs.containsKey(TextAttribute.FONT));
    }

    @Test
    public void testGetBoundingBoxReturnsValidRectangle() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(2);

        Rectangle box = textItem.getBoundingBox(g, observer, 1.0f, style);
        assertNotNull(box);
        assertTrue(box.width >= 0);
        assertTrue(box.height >= 0);
    }

    @Test
    public void testDrawDoesNotThrow() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        ImageObserver observer = (img, flags, x, y, w, h) -> true;
        Style style = Style.getStyle(2);

        assertDoesNotThrow(() -> textItem.draw(0, 0, 1.0f, g, style, observer));
    }

    @Test
    public void testToStringIncludesText() {
        String str = textItem.toString();
        assertTrue(str.contains("TextItem"));
        assertTrue(str.contains("Hello world"));
    }
}
