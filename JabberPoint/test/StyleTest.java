import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.awt.Font;

import static org.junit.jupiter.api.Assertions.*;

public class StyleTest {

    private Style style;

    @BeforeEach
    public void setup() {
        // Create a custom style for testing
        style = new Style(40, Color.RED, 32, 10);
    }

    @Test
    public void testConstructorInitializesFields() {
        assertNotNull(style);
        assertEquals(Color.RED, style.color);
        assertEquals(40, style.indent);
        assertEquals(32, style.fontSize);
        assertEquals(10, style.leading);
        assertNotNull(style.font);
        assertEquals("Helvetica", style.font.getName());
    }

    @Test
    public void testGetFontReturnsScaledFont() {
        Font scaledFont = style.getFont(1.5f);
        assertEquals((int)(32 * 1.5f), scaledFont.getSize());
    }

    @Test
    public void testToStringContainsStyleDetails() {
        String result = style.toString();
        assertTrue(result.contains("40"));
        assertTrue(result.contains("RED"));
        assertTrue(result.contains("32"));
        assertTrue(result.contains("10"));
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
}

