import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class StyleTest {

    private Style style;

    @BeforeEach
    public void setup() {
        // Create a custom style for testing
        style = new Style(40, Color.RED, 32, 10);
    }


    @Test
    public void testGetFontReturnsScaledFont() {
        Font scaledFont = style.getFont(1.5f);
        assertEquals((int)(32 * 1.5f), scaledFont.getSize());
    }

    @Test
    public void testToStringContainsStyleDetails() {
        String result = style.toString();
        assertTrue(result.contains("40")); // indent
        assertTrue(result.contains("32")); // fontSize
        assertTrue(result.contains("10")); // leading

        assertTrue(result.contains("Color")); // avoids assuming "RED"
    }

    @Test
    public void testCreateStylesAndGetStyle() {
        Style.createStyles(); // initialize static array
        Style s0 = Style.getStyle(0);
        Style s1 = Style.getStyle(1);
        Style s4 = Style.getStyle(4);

        assertNotNull(s0);
        assertNotNull(s1);
        assertNotNull(s4);
        assertEquals(Color.red, s0.color);
        assertEquals(Color.blue, s1.color);
        assertEquals(Color.black, s4.color);
    }

    @Test
    public void testGetStyleLevelOutOfBounds() {
        Style.createStyles();
        Style max = Style.getStyle(10); // level beyond array size
        assertNotNull(max);
        assertEquals(Color.black, max.color);
    }

    @Test
    public void testCreateAccessorReturnsXMLAccessor() {
        assertTrue(style.createAccessor() instanceof XMLAccessor);
    }

    @Test
    public void testGetFontWithZeroScaleReturnsZeroSizeFont() {
        Style style = new Style(10, Color.BLACK, 0, 10);
        Font font = style.getFont(0.0f);
        assertEquals(0, font.getSize());
    }

    @Test
    public void testGetFontWithNegativeScaleReturnsNegativeFontSize() {
        Style style = new Style(10, Color.BLACK, 30, 10);
        Font font = style.getFont(-1.0f);
        int expected = font.getSize(); // capture actual result
        assertTrue(expected <= 0);
    }



    @Test
    public void testGetFontWithVeryLargeScale() {
        Style style = new Style(10, Color.BLACK, 5, 10);
        Font font = style.getFont(100.0f);
        assertEquals(500, font.getSize());
    }

    @Test
    public void testGetStyleWithNegativeLevelThrows() {
        Style.createStyles();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Style.getStyle(-5));
    }


    @Test
    public void testGetStyleWithExcessivelyHighLevelReturnsFallback() {
        Style.createStyles();
        Style style = Style.getStyle(999);
        assertNotNull(style);
        assertEquals(Color.black, style.color);
    }

    @Test
    public void testCreateStylesIsIdempotent() {
        Style.createStyles();
        Style[] first = getStylesArray();
        Style.createStyles();
        Style[] second = getStylesArray();

        assertEquals(first.length, second.length);

        for (int i = 0; i < first.length; i++) {
            assertEquals(first[i].indent, second[i].indent);
            assertEquals(first[i].color, second[i].color);
            assertEquals(first[i].fontSize, second[i].fontSize);
            assertEquals(first[i].leading, second[i].leading);
            assertEquals(first[i].font.getSize(), second[i].font.getSize());
        }
    }


    @Test
    public void testToStringHandlesNullColorAndFontGracefully() throws Exception {
        Style style = new Style(20, Color.RED, 30, 10);

        Field colorField = Style.class.getDeclaredField("color");
        Field fontField = Style.class.getDeclaredField("font");
        colorField.setAccessible(true);
        fontField.setAccessible(true);

        colorField.set(style, null);
        fontField.set(style, null);

        String result = style.toString();
        assertNotNull(result);
        assertTrue(result.contains("20"));
    }

    // Helper method
    private Style[] getStylesArray() {
        try {
            Field f = Style.class.getDeclaredField("styles");
            f.setAccessible(true);
            return (Style[]) f.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}

