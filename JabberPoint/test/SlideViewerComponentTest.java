import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SlideViewerComponentTest {

    private SlideViewerComponent component;

    @BeforeEach
    public void setup() {
        Presentation presentation = new Presentation();
        component = new SlideViewerComponent(presentation, new SlideViewerFrame("Test Frame", presentation));
    }

    @Test
    public void testComponentInitializesAndHasPreferredSize() {
        assertNotNull(component);
        Dimension size = component.getPreferredSize();
        assertEquals(SlideViewerComponent.WIDTH, size.width);
        assertEquals(SlideViewerComponent.HEIGHT, size.height);
    }

    @Test
    public void testUpdateDoesNotThrow() {
        Slide slide = new Slide();
        slide.setTitle("Slide A");

        assertDoesNotThrow(() -> component.update(slide));
    }

    @Test
    public void testPaintComponentDoesNotThrow() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testPaintComponentAfterUpdate() {
        Slide slide = new Slide();
        slide.setTitle("Slide B");
        component.update(slide);

        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }
}
