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

    @Test
    public void testUpdateWithNullSlideDoesNotThrow() {
        assertDoesNotThrow(() -> component.update(null));
    }

    @Test
    public void testPaintComponentWithNullGraphicsThrows() {
        assertThrows(NullPointerException.class, () -> component.paintComponent(null));
    }

    @Test
    public void testUpdateWithMultipleSlidesSequentially() {
        for (int i = 0; i < 5; i++) {
            Slide slide = new Slide();
            slide.setTitle("Slide " + i);
            slide.append(1, "Content " + i);
            assertDoesNotThrow(() -> component.update(slide));
        }
    }

    @Test
    public void testPaintComponentWithLargeSlideContent() {
        Slide bigSlide = new Slide();
        bigSlide.setTitle("Big One");
        for (int i = 0; i < 50; i++) {
            bigSlide.append(1, "Item " + i);
        }

        component.update(bigSlide);

        Graphics g = new BufferedImage(1600, 1200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testPaintComponentWithTinyResolution() {
        Slide slide = new Slide();
        slide.setTitle("Tiny Draw");
        slide.append(1, "Item");

        component.update(slide);

        Graphics g = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testUpdateAndDrawQuicklyInLoop() {
        for (int i = 0; i < 3; i++) {
            Slide slide = new Slide();
            slide.setTitle("Quick " + i);
            slide.append(1, "Text " + i);
            component.update(slide);

            Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
            assertDoesNotThrow(() -> component.paintComponent(g));
        }
    }
}
