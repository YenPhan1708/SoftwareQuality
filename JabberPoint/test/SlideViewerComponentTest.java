import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Slide.*;
import Presentation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class SlideViewerComponentTest {

    private SlideViewerComponent component;
    private Presentation presentation;

    @BeforeEach
    public void setup() {
        presentation = new Presentation();
        component = new SlideViewerComponent(presentation, null); // Frame not needed for component logic
    }

    @Test
    public void testComponentInitializesAndHasPreferredSize() {
        assertNotNull(component);
        Dimension size = component.getPreferredSize();
        assertEquals(Slide.WIDTH, size.width);
        assertEquals(Slide.HEIGHT, size.height);
    }

    @Test
    public void testUpdateWithSlideDoesNotThrow() {
        Slide slide = new Slide();
        slide.setTitle("Slide A");

        assertDoesNotThrow(() -> component.update(slide));
    }

    @Test
    public void testPaintComponentDoesNotThrowWithNullSlide() {
        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g)); // should handle null gracefully
    }

    @Test
    public void testPaintComponentAfterSlideUpdate() {
        Slide slide = new Slide();
        slide.setTitle("Slide B");
        component.update(slide);

        Graphics g = new BufferedImage(1200, 800, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testUpdateWithNullSlideDoesNotThrow() {
        assertDoesNotThrow(() -> component.update((Slide) null));
    }

    @Test
    public void testPaintComponentWithNullGraphicsThrows() {
        assertThrows(NullPointerException.class, () -> component.paintComponent(null));
    }

    @Test
    public void testSequentialSlideUpdates() {
        for (int i = 0; i < 5; i++) {
            Slide slide = new Slide();
            slide.setTitle("Slide " + i);
            slide.appendTextItem(1, "Content " + i);
            assertDoesNotThrow(() -> component.update(slide));
        }
    }

    @Test
    public void testPaintComponentWithManyItems() {
        Slide bigSlide = new Slide();
        bigSlide.setTitle("Big One");

        for (int i = 0; i < 50; i++) {
            bigSlide.appendTextItem(1, "Item " + i);
        }

        component.update(bigSlide);
        Graphics g = new BufferedImage(1600, 1200, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testPaintComponentAtLowResolution() {
        Slide slide = new Slide();
        slide.setTitle("Tiny Draw");
        slide.appendTextItem(1, "Item");

        component.update(slide);
        Graphics g = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB).getGraphics();
        assertDoesNotThrow(() -> component.paintComponent(g));
    }

    @Test
    public void testFastUpdateAndDrawLoop() {
        for (int i = 0; i < 3; i++) {
            Slide slide = new Slide();
            slide.setTitle("Quick " + i);
            slide.appendTextItem(1, "Text " + i);
            component.update(slide);

            Graphics g = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB).getGraphics();
            assertDoesNotThrow(() -> component.paintComponent(g));
        }
    }
}
