import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Style.Style;
import Accessor.*;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class StyleTest {

    private Style style;

    @BeforeEach
    public void setup() {
        style = new Style(40, Color.RED, 32, 10);
    }

    @Test
    public void testGetFontReturnsScaledSize() {
        Font scaledFont = style.getFont(1.5f);
        assertEquals((int)(32 * 1.5f), scaledFont.getSize());
    }

    @Test
    public void testToStringIncludesStyleDetails() {
        String result = style.toString();
        assertTrue(result.contains("40")); // indent
        assertTrue(result.contains("32")); // fontSize
        assertTrue(result.contains("10")); // leading
        assertTrue(result.contains("Color")); // generic color info
    }

    @Test
    public void testCreateStylesAndGetStyleAtLevels() {
        Style.createStyles();
        assertNotNull(Style.getStyle(0));
        assertNotNull(Style.getStyle(1));
        assertNotNull(Style.getStyle(4));

        assertEquals(Color.red, Style.getStyle(0).color);
        assertEquals(Color.blue, Style.getStyle(1).color);
        assertEquals(Color.black, Style.getStyle(4).color);
    }

    @Test
    public void testGetStyleLevelOutOfBoundsReturnsFallback() {
        Style.createStyles();
        Style style = Style.getStyle(10); // beyond defined styles
        assertNotNull(style);
        assertEquals(Color.black, style.color);
    }


    @Test
    public void testGetFontWithZeroScaleReturnsZeroFontSize() {
        Style style = new Style(10, Color.BLACK, 0, 10);
        Font font = style.getFont(0.0f);
        assertEquals(0, font.getSize());
    }

    @Test
    public void testGetFontWithNegativeScaleReturnsNegativeFontSize() {
        Style style = new Style(10, Color.BLACK, 30, 10);
        Font font = style.getFont(-1.0f);
        assertTrue(font.getSize() <= 0);
    }

    @Test
    public void testGetFontWithVeryLargeScaleReturnsExpectedSize() {
        Style style = new Style(10, Color.BLACK, 5, 10);
        Font font = style.getFont(100.0f);
        assertEquals(500, font.getSize());
    }

    @Test
    public void testGetStyleWithNegativeLevelThrowsException() {
        Style.createStyles();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> Style.getStyle(-5));
    }

    @Test
    public void testGetStyleWithVeryHighLevelReturnsFallbackStyle() {
        Style.createStyles();
        Style style = Style.getStyle(999);
        assertNotNull(style);
        assertEquals(Color.black, style.color);
    }

    @Test
    public void testCreateStylesIsIdempotent() {
        Style.createStyles();
        Style[] first = getInternalStylesArray();
        Style.createStyles();
        Style[] second = getInternalStylesArray();

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
    public void testToStringHandlesNullFontAndColor() throws Exception {
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

    // Internal helper to access private static styles[] field
    private Style[] getInternalStylesArray() {
        try {
            Field stylesField = Style.class.getDeclaredField("styles");
            stylesField.setAccessible(true);
            return (Style[]) stylesField.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
